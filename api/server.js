const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const { v4: uuidv4 } = require('uuid');

const app = express();
const PORT = process.env.PORT || 3000;
const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key-change-in-production';

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// In-memory storage (replace with database in production)
let users = [];
let pets = [];
let consultations = [];
let prescriptions = [];
let chatMessages = [];

// Mock veterinary clinics data
const mockClinics = [
  {
    id: '1',
    name: 'Happy Paws Veterinary Clinic',
    address: '123 Main Street, New York, NY 10001',
    latitude: 40.7128,
    longitude: -74.0060,
    phone: '+1-555-0101',
    rating: 4.8,
    isEmergency: false,
    openingHours: 'Mon-Fri: 9AM-6PM, Sat: 10AM-4PM'
  },
  {
    id: '2',
    name: 'Emergency Pet Care Center',
    address: '456 Oak Avenue, New York, NY 10002',
    latitude: 40.7580,
    longitude: -73.9855,
    phone: '+1-555-0102',
    rating: 4.9,
    isEmergency: true,
    openingHours: '24/7'
  },
  {
    id: '3',
    name: 'City Animal Hospital',
    address: '789 Park Boulevard, New York, NY 10003',
    latitude: 40.7505,
    longitude: -73.9934,
    phone: '+1-555-0103',
    rating: 4.7,
    isEmergency: false,
    openingHours: 'Mon-Sun: 8AM-8PM'
  },
  {
    id: '4',
    name: 'Pet Wellness Center',
    address: '321 Broadway, New York, NY 10004',
    latitude: 40.7282,
    longitude: -74.0776,
    phone: '+1-555-0104',
    rating: 4.6,
    isEmergency: false,
    openingHours: 'Mon-Fri: 8AM-7PM, Sat-Sun: 9AM-5PM'
  }
];

// Mock veterinarians
const mockVets = [
  { id: 'vet_001', name: 'Dr. Sarah Johnson', specialization: 'General Practice', rating: 4.9 },
  { id: 'vet_002', name: 'Dr. Michael Chen', specialization: 'Emergency Care', rating: 4.8 },
  { id: 'vet_003', name: 'Dr. Emily Rodriguez', specialization: 'Surgery', rating: 4.9 },
  { id: 'vet_004', name: 'Dr. James Wilson', specialization: 'Dermatology', rating: 4.7 }
];

// Helper function to verify JWT token
const verifyToken = (req, res, next) => {
  const token = req.headers['authorization']?.split(' ')[1]; // Bearer TOKEN

  if (!token) {
    return res.status(401).json({ error: 'No token provided' });
  }

  jwt.verify(token, JWT_SECRET, (err, decoded) => {
    if (err) {
      return res.status(401).json({ error: 'Invalid token' });
    }
    req.userId = decoded.userId;
    req.userEmail = decoded.email;
    next();
  });
};

// Helper function to calculate distance between two coordinates
const calculateDistance = (lat1, lon1, lat2, lon2) => {
  const R = 6371; // Earth's radius in km
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLon = (lon2 - lon1) * Math.PI / 180;
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c;
};

// ==================== AUTHENTICATION ENDPOINTS ====================

// Register new user
app.post('/api/auth/register', async (req, res) => {
  try {
    const { name, email, password, phone } = req.body;

    if (!name || !email || !password || !phone) {
      return res.status(400).json({ error: 'All fields are required' });
    }

    // Check if user already exists
    const existingUser = users.find(u => u.email === email);
    if (existingUser) {
      return res.status(400).json({ error: 'User already exists' });
    }

    // Hash password
    const hashedPassword = await bcrypt.hash(password, 10);

    const newUser = {
      id: uuidv4(),
      name,
      email,
      password: hashedPassword,
      phone,
      createdAt: new Date().toISOString()
    };

    users.push(newUser);

    // Generate JWT token
    const token = jwt.sign(
      { userId: newUser.id, email: newUser.email },
      JWT_SECRET,
      { expiresIn: '7d' }
    );

    res.status(201).json({
      message: 'User registered successfully',
      token,
      user: {
        id: newUser.id,
        name: newUser.name,
        email: newUser.email,
        phone: newUser.phone
      }
    });
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
});

// Login user
app.post('/api/auth/login', async (req, res) => {
  try {
    const { email, password } = req.body;

    if (!email || !password) {
      return res.status(400).json({ error: 'Email and password are required' });
    }

    const user = users.find(u => u.email === email);
    if (!user) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }

    // Verify password
    const isValidPassword = await bcrypt.compare(password, user.password);
    if (!isValidPassword) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }

    // Generate JWT token
    const token = jwt.sign(
      { userId: user.id, email: user.email },
      JWT_SECRET,
      { expiresIn: '7d' }
    );

    res.json({
      message: 'Login successful',
      token,
      user: {
        id: user.id,
        name: user.name,
        email: user.email,
        phone: user.phone
      }
    });
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
});

// Get current user profile
app.get('/api/auth/me', verifyToken, (req, res) => {
  const user = users.find(u => u.id === req.userId);
  if (!user) {
    return res.status(404).json({ error: 'User not found' });
  }

  res.json({
    id: user.id,
    name: user.name,
    email: user.email,
    phone: user.phone
  });
});

// ==================== PET ENDPOINTS ====================

// Get all pets for current user
app.get('/api/pets', verifyToken, (req, res) => {
  const userPets = pets.filter(p => p.userId === req.userId);
  res.json(userPets);
});

// Get single pet by ID
app.get('/api/pets/:petId', verifyToken, (req, res) => {
  const pet = pets.find(p => p.id === req.params.petId && p.userId === req.userId);
  if (!pet) {
    return res.status(404).json({ error: 'Pet not found' });
  }
  res.json(pet);
});

// Register new pet
app.post('/api/pets', verifyToken, (req, res) => {
  try {
    const { name, breed, age, species, medicalHistory, photoUri } = req.body;

    if (!name || !breed || !age || !species) {
      return res.status(400).json({ error: 'Name, breed, age, and species are required' });
    }

    const newPet = {
      id: uuidv4(),
      userId: req.userId,
      name,
      breed,
      age: parseInt(age),
      species: species || 'Dog',
      medicalHistory: medicalHistory || '',
      photoUri: photoUri || null,
      createdAt: new Date().toISOString()
    };

    pets.push(newPet);
    res.status(201).json(newPet);
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
});

// Update pet
app.put('/api/pets/:petId', verifyToken, (req, res) => {
  try {
    const petIndex = pets.findIndex(
      p => p.id === req.params.petId && p.userId === req.userId
    );

    if (petIndex === -1) {
      return res.status(404).json({ error: 'Pet not found' });
    }

    const { name, breed, age, species, medicalHistory, photoUri } = req.body;
    pets[petIndex] = {
      ...pets[petIndex],
      name: name || pets[petIndex].name,
      breed: breed || pets[petIndex].breed,
      age: age ? parseInt(age) : pets[petIndex].age,
      species: species || pets[petIndex].species,
      medicalHistory: medicalHistory !== undefined ? medicalHistory : pets[petIndex].medicalHistory,
      photoUri: photoUri !== undefined ? photoUri : pets[petIndex].photoUri,
      updatedAt: new Date().toISOString()
    };

    res.json(pets[petIndex]);
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
});

// Delete pet
app.delete('/api/pets/:petId', verifyToken, (req, res) => {
  const petIndex = pets.findIndex(
    p => p.id === req.params.petId && p.userId === req.userId
  );

  if (petIndex === -1) {
    return res.status(404).json({ error: 'Pet not found' });
  }

  pets.splice(petIndex, 1);
  res.json({ message: 'Pet deleted successfully' });
});

// ==================== CONSULTATION ENDPOINTS ====================

// Get all consultations for current user
app.get('/api/consultations', verifyToken, (req, res) => {
  const userConsultations = consultations
    .filter(c => c.userId === req.userId)
    .map(consultation => {
      const consultationMessages = chatMessages.filter(m => m.consultationId === consultation.id);
      return {
        ...consultation,
        messages: consultationMessages,
        lastMessage: consultationMessages.length > 0 
          ? consultationMessages[consultationMessages.length - 1] 
          : null
      };
    })
    .sort((a, b) => new Date(b.startTime) - new Date(a.startTime));

  res.json(userConsultations);
});

// Get single consultation by ID
app.get('/api/consultations/:consultationId', verifyToken, (req, res) => {
  const consultation = consultations.find(
    c => c.id === req.params.consultationId && c.userId === req.userId
  );

  if (!consultation) {
    return res.status(404).json({ error: 'Consultation not found' });
  }

  const consultationMessages = chatMessages.filter(m => m.consultationId === consultation.id);
  res.json({
    ...consultation,
    messages: consultationMessages
  });
});

// Start new consultation
app.post('/api/consultations', verifyToken, (req, res) => {
  try {
    const { petId, type } = req.body;

    if (!petId || !type) {
      return res.status(400).json({ error: 'Pet ID and consultation type are required' });
    }

    // Verify pet belongs to user
    const pet = pets.find(p => p.id === petId && p.userId === req.userId);
    if (!pet) {
      return res.status(404).json({ error: 'Pet not found' });
    }

    // Assign random vet
    const randomVet = mockVets[Math.floor(Math.random() * mockVets.length)];

    const newConsultation = {
      id: uuidv4(),
      userId: req.userId,
      petId,
      vetId: randomVet.id,
      vetName: randomVet.name,
      type: type.toUpperCase(), // CHAT or VIDEO_CALL
      status: 'ACTIVE',
      startTime: new Date().toISOString(),
      endTime: null,
      createdAt: new Date().toISOString()
    };

    consultations.push(newConsultation);
    res.status(201).json(newConsultation);
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
});

// End consultation
app.put('/api/consultations/:consultationId/end', verifyToken, (req, res) => {
  const consultationIndex = consultations.findIndex(
    c => c.id === req.params.consultationId && c.userId === req.userId
  );

  if (consultationIndex === -1) {
    return res.status(404).json({ error: 'Consultation not found' });
  }

  consultations[consultationIndex].status = 'COMPLETED';
  consultations[consultationIndex].endTime = new Date().toISOString();

  res.json(consultations[consultationIndex]);
});

// Send chat message
app.post('/api/consultations/:consultationId/messages', verifyToken, (req, res) => {
  try {
    const { message } = req.body;
    const consultation = consultations.find(
      c => c.id === req.params.consultationId && c.userId === req.userId
    );

    if (!consultation) {
      return res.status(404).json({ error: 'Consultation not found' });
    }

    if (consultation.status !== 'ACTIVE') {
      return res.status(400).json({ error: 'Consultation is not active' });
    }

    const newMessage = {
      id: uuidv4(),
      consultationId: req.params.consultationId,
      senderId: req.userId,
      senderName: users.find(u => u.id === req.userId)?.name || 'User',
      isFromVet: false,
      message,
      timestamp: new Date().toISOString()
    };

    chatMessages.push(newMessage);

    // Simulate vet response after 2 seconds (for demo)
    setTimeout(() => {
      const vetResponse = {
        id: uuidv4(),
        consultationId: req.params.consultationId,
        senderId: consultation.vetId,
        senderName: consultation.vetName,
        isFromVet: true,
        message: 'Thank you for your message. I\'m reviewing your pet\'s information and will provide advice shortly.',
        timestamp: new Date().toISOString()
      };
      chatMessages.push(vetResponse);
    }, 2000);

    res.status(201).json(newMessage);
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
});

// Get messages for consultation
app.get('/api/consultations/:consultationId/messages', verifyToken, (req, res) => {
  const consultation = consultations.find(
    c => c.id === req.params.consultationId && c.userId === req.userId
  );

  if (!consultation) {
    return res.status(404).json({ error: 'Consultation not found' });
  }

  const messages = chatMessages
    .filter(m => m.consultationId === req.params.consultationId)
    .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

  res.json(messages);
});

// ==================== VETERINARY CLINICS ENDPOINTS ====================

// Find nearby veterinary clinics
app.get('/api/vets/nearby', verifyToken, (req, res) => {
  try {
    const { latitude, longitude } = req.query;

    if (!latitude || !longitude) {
      return res.status(400).json({ error: 'Latitude and longitude are required' });
    }

    const userLat = parseFloat(latitude);
    const userLon = parseFloat(longitude);

    const clinicsWithDistance = mockClinics.map(clinic => {
      const distance = calculateDistance(
        userLat,
        userLon,
        clinic.latitude,
        clinic.longitude
      );
      return {
        ...clinic,
        distance: parseFloat(distance.toFixed(2))
      };
    }).sort((a, b) => a.distance - b.distance);

    res.json(clinicsWithDistance);
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
});

// Get all clinics
app.get('/api/vets/clinics', verifyToken, (req, res) => {
  res.json(mockClinics);
});

// Get single clinic by ID
app.get('/api/vets/clinics/:clinicId', verifyToken, (req, res) => {
  const clinic = mockClinics.find(c => c.id === req.params.clinicId);
  if (!clinic) {
    return res.status(404).json({ error: 'Clinic not found' });
  }
  res.json(clinic);
});

// Get available veterinarians
app.get('/api/vets/doctors', verifyToken, (req, res) => {
  res.json(mockVets);
});

// ==================== PRESCRIPTION ENDPOINTS ====================

// Get prescriptions for a pet
app.get('/api/prescriptions', verifyToken, (req, res) => {
  const { petId } = req.query;

  if (!petId) {
    return res.status(400).json({ error: 'Pet ID is required' });
  }

  // Verify pet belongs to user
  const pet = pets.find(p => p.id === petId && p.userId === req.userId);
  if (!pet) {
    return res.status(404).json({ error: 'Pet not found' });
  }

  const petPrescriptions = prescriptions
    .filter(p => p.petId === petId)
    .sort((a, b) => new Date(b.date) - new Date(a.date));

  res.json(petPrescriptions);
});

// Get single prescription by ID
app.get('/api/prescriptions/:prescriptionId', verifyToken, (req, res) => {
  const prescription = prescriptions.find(p => p.id === req.params.prescriptionId);
  if (!prescription) {
    return res.status(404).json({ error: 'Prescription not found' });
  }

  // Verify pet belongs to user
  const pet = pets.find(p => p.id === prescription.petId && p.userId === req.userId);
  if (!pet) {
    return res.status(403).json({ error: 'Access denied' });
  }

  res.json(prescription);
});

// Create prescription (typically done by vet, but included for demo)
app.post('/api/prescriptions', verifyToken, (req, res) => {
  try {
    const { consultationId, petId, medications, instructions } = req.body;

    if (!consultationId || !petId || !medications || !Array.isArray(medications)) {
      return res.status(400).json({ error: 'Consultation ID, pet ID, and medications are required' });
    }

    // Verify consultation exists and belongs to user
    const consultation = consultations.find(
      c => c.id === consultationId && c.userId === req.userId
    );
    if (!consultation) {
      return res.status(404).json({ error: 'Consultation not found' });
    }

    const newPrescription = {
      id: uuidv4(),
      consultationId,
      petId,
      vetId: consultation.vetId,
      vetName: consultation.vetName,
      medications,
      instructions: instructions || '',
      date: new Date().toISOString(),
      isDelivered: false,
      createdAt: new Date().toISOString()
    };

    prescriptions.push(newPrescription);
    res.status(201).json(newPrescription);
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
});

// Update prescription delivery status
app.put('/api/prescriptions/:prescriptionId/delivery', verifyToken, (req, res) => {
  const prescriptionIndex = prescriptions.findIndex(p => p.id === req.params.prescriptionId);
  if (prescriptionIndex === -1) {
    return res.status(404).json({ error: 'Prescription not found' });
  }

  // Verify pet belongs to user
  const pet = pets.find(p => p.id === prescriptions[prescriptionIndex].petId && p.userId === req.userId);
  if (!pet) {
    return res.status(403).json({ error: 'Access denied' });
  }

  prescriptions[prescriptionIndex].isDelivered = true;
  prescriptions[prescriptionIndex].deliveredAt = new Date().toISOString();

  res.json(prescriptions[prescriptionIndex]);
});

// ==================== HEALTH CHECK ====================

app.get('/api/health', (req, res) => {
  res.json({
    status: 'OK',
    message: 'Online Vet Hospital API is running',
    timestamp: new Date().toISOString()
  });
});

// ==================== START SERVER ====================

app.listen(PORT, () => {
  console.log(`🚀 Online Vet Hospital API server running on port ${PORT}`);
  console.log(`📝 API Documentation: http://localhost:${PORT}/api/health`);
  console.log(`\nAvailable endpoints:`);
  console.log(`  POST   /api/auth/register`);
  console.log(`  POST   /api/auth/login`);
  console.log(`  GET    /api/auth/me`);
  console.log(`  GET    /api/pets`);
  console.log(`  POST   /api/pets`);
  console.log(`  GET    /api/consultations`);
  console.log(`  POST   /api/consultations`);
  console.log(`  GET    /api/vets/nearby`);
  console.log(`  GET    /api/prescriptions`);
});
