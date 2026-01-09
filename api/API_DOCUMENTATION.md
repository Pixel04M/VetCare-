# Online Vet Hospital API - Complete Documentation

## Base URL
```
http://localhost:3000
```

## Authentication

All endpoints (except `/api/auth/register`, `/api/auth/login`, and `/api/health`) require JWT authentication.

Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

---

## Endpoints

### 🔐 Authentication

#### 1. Register User
**POST** `/api/auth/register`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "+1234567890"
}
```

**Response (201):**
```json
{
  "message": "User registered successfully",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890"
  }
}
```

#### 2. Login
**POST** `/api/auth/login`

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890"
  }
}
```

#### 3. Get Current User
**GET** `/api/auth/me`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "id": "uuid",
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890"
}
```

---

### 🐾 Pets

#### 1. Get All Pets
**GET** `/api/pets`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
[
  {
    "id": "uuid",
    "userId": "user-uuid",
    "name": "Buddy",
    "breed": "Golden Retriever",
    "age": 3,
    "species": "Dog",
    "medicalHistory": "No known allergies",
    "photoUri": null,
    "createdAt": "2024-01-01T00:00:00.000Z"
  }
]
```

#### 2. Get Pet by ID
**GET** `/api/pets/:petId`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "id": "uuid",
  "userId": "user-uuid",
  "name": "Buddy",
  "breed": "Golden Retriever",
  "age": 3,
  "species": "Dog",
  "medicalHistory": "No known allergies",
  "photoUri": null,
  "createdAt": "2024-01-01T00:00:00.000Z"
}
```

#### 3. Register Pet
**POST** `/api/pets`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Buddy",
  "breed": "Golden Retriever",
  "age": 3,
  "species": "Dog",
  "medicalHistory": "No known allergies",
  "photoUri": "https://example.com/photo.jpg"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "userId": "user-uuid",
  "name": "Buddy",
  "breed": "Golden Retriever",
  "age": 3,
  "species": "Dog",
  "medicalHistory": "No known allergies",
  "photoUri": "https://example.com/photo.jpg",
  "createdAt": "2024-01-01T00:00:00.000Z"
}
```

#### 4. Update Pet
**PUT** `/api/pets/:petId`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Buddy",
  "breed": "Golden Retriever",
  "age": 4
}
```

**Response (200):**
```json
{
  "id": "uuid",
  "userId": "user-uuid",
  "name": "Buddy",
  "breed": "Golden Retriever",
  "age": 4,
  "species": "Dog",
  "medicalHistory": "No known allergies",
  "photoUri": null,
  "createdAt": "2024-01-01T00:00:00.000Z",
  "updatedAt": "2024-01-02T00:00:00.000Z"
}
```

#### 5. Delete Pet
**DELETE** `/api/pets/:petId`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "message": "Pet deleted successfully"
}
```

---

### 💬 Consultations

#### 1. Get All Consultations
**GET** `/api/consultations`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
[
  {
    "id": "uuid",
    "userId": "user-uuid",
    "petId": "pet-uuid",
    "vetId": "vet_001",
    "vetName": "Dr. Sarah Johnson",
    "type": "CHAT",
    "status": "ACTIVE",
    "startTime": "2024-01-01T00:00:00.000Z",
    "endTime": null,
    "messages": [...],
    "lastMessage": {...}
  }
]
```

#### 2. Get Consultation by ID
**GET** `/api/consultations/:consultationId`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "id": "uuid",
  "userId": "user-uuid",
  "petId": "pet-uuid",
  "vetId": "vet_001",
  "vetName": "Dr. Sarah Johnson",
  "type": "CHAT",
  "status": "ACTIVE",
  "startTime": "2024-01-01T00:00:00.000Z",
  "endTime": null,
  "messages": [
    {
      "id": "msg-uuid",
      "consultationId": "consultation-uuid",
      "senderId": "user-uuid",
      "senderName": "John Doe",
      "isFromVet": false,
      "message": "My dog has been coughing",
      "timestamp": "2024-01-01T00:00:00.000Z"
    }
  ]
}
```

#### 3. Start Consultation
**POST** `/api/consultations`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "petId": "pet-uuid",
  "type": "CHAT"
}
```

**Note:** `type` can be `"CHAT"` or `"VIDEO_CALL"`

**Response (201):**
```json
{
  "id": "uuid",
  "userId": "user-uuid",
  "petId": "pet-uuid",
  "vetId": "vet_001",
  "vetName": "Dr. Sarah Johnson",
  "type": "CHAT",
  "status": "ACTIVE",
  "startTime": "2024-01-01T00:00:00.000Z",
  "endTime": null,
  "createdAt": "2024-01-01T00:00:00.000Z"
}
```

#### 4. End Consultation
**PUT** `/api/consultations/:consultationId/end`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "id": "uuid",
  "userId": "user-uuid",
  "petId": "pet-uuid",
  "vetId": "vet_001",
  "vetName": "Dr. Sarah Johnson",
  "type": "CHAT",
  "status": "COMPLETED",
  "startTime": "2024-01-01T00:00:00.000Z",
  "endTime": "2024-01-01T01:00:00.000Z"
}
```

#### 5. Send Chat Message
**POST** `/api/consultations/:consultationId/messages`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "message": "My dog has been coughing for 2 days. What should I do?"
}
```

**Response (201):**
```json
{
  "id": "msg-uuid",
  "consultationId": "consultation-uuid",
  "senderId": "user-uuid",
  "senderName": "John Doe",
  "isFromVet": false,
  "message": "My dog has been coughing for 2 days. What should I do?",
  "timestamp": "2024-01-01T00:00:00.000Z"
}
```

**Note:** The API automatically generates a vet response after 2 seconds for demo purposes.

#### 6. Get Messages
**GET** `/api/consultations/:consultationId/messages`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
[
  {
    "id": "msg-uuid",
    "consultationId": "consultation-uuid",
    "senderId": "user-uuid",
    "senderName": "John Doe",
    "isFromVet": false,
    "message": "My dog has been coughing",
    "timestamp": "2024-01-01T00:00:00.000Z"
  },
  {
    "id": "msg-uuid-2",
    "consultationId": "consultation-uuid",
    "senderId": "vet_001",
    "senderName": "Dr. Sarah Johnson",
    "isFromVet": true,
    "message": "Thank you for your message...",
    "timestamp": "2024-01-01T00:00:02.000Z"
  }
]
```

---

### 🏥 Veterinary Clinics

#### 1. Find Nearby Clinics
**GET** `/api/vets/nearby?latitude=40.7128&longitude=-74.0060`

**Headers:**
```
Authorization: Bearer <token>
```

**Query Parameters:**
- `latitude` (required): User's latitude
- `longitude` (required): User's longitude

**Response (200):**
```json
[
  {
    "id": "1",
    "name": "Happy Paws Veterinary Clinic",
    "address": "123 Main Street, New York, NY 10001",
    "latitude": 40.7128,
    "longitude": -74.0060,
    "phone": "+1-555-0101",
    "rating": 4.8,
    "isEmergency": false,
    "openingHours": "Mon-Fri: 9AM-6PM, Sat: 10AM-4PM",
    "distance": 0.5
  }
]
```

**Note:** Results are sorted by distance (nearest first).

#### 2. Get All Clinics
**GET** `/api/vets/clinics`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
[
  {
    "id": "1",
    "name": "Happy Paws Veterinary Clinic",
    "address": "123 Main Street, New York, NY 10001",
    "latitude": 40.7128,
    "longitude": -74.0060,
    "phone": "+1-555-0101",
    "rating": 4.8,
    "isEmergency": false,
    "openingHours": "Mon-Fri: 9AM-6PM, Sat: 10AM-4PM"
  }
]
```

#### 3. Get Clinic by ID
**GET** `/api/vets/clinics/:clinicId`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "id": "1",
  "name": "Happy Paws Veterinary Clinic",
  "address": "123 Main Street, New York, NY 10001",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "phone": "+1-555-0101",
  "rating": 4.8,
  "isEmergency": false,
  "openingHours": "Mon-Fri: 9AM-6PM, Sat: 10AM-4PM"
}
```

#### 4. Get Available Veterinarians
**GET** `/api/vets/doctors`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
[
  {
    "id": "vet_001",
    "name": "Dr. Sarah Johnson",
    "specialization": "General Practice",
    "rating": 4.9
  }
]
```

---

### 💊 Prescriptions

#### 1. Get Prescriptions
**GET** `/api/prescriptions?petId=pet-uuid`

**Headers:**
```
Authorization: Bearer <token>
```

**Query Parameters:**
- `petId` (required): Pet ID

**Response (200):**
```json
[
  {
    "id": "prescription-uuid",
    "consultationId": "consultation-uuid",
    "petId": "pet-uuid",
    "vetId": "vet_001",
    "vetName": "Dr. Sarah Johnson",
    "medications": [
      {
        "name": "Antibiotic",
        "dosage": "500mg",
        "frequency": "Twice daily",
        "duration": "7 days"
      }
    ],
    "instructions": "Give with food after meals",
    "date": "2024-01-01T00:00:00.000Z",
    "isDelivered": false,
    "createdAt": "2024-01-01T00:00:00.000Z"
  }
]
```

#### 2. Get Prescription by ID
**GET** `/api/prescriptions/:prescriptionId`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "id": "prescription-uuid",
  "consultationId": "consultation-uuid",
  "petId": "pet-uuid",
  "vetId": "vet_001",
  "vetName": "Dr. Sarah Johnson",
  "medications": [
    {
      "name": "Antibiotic",
      "dosage": "500mg",
      "frequency": "Twice daily",
      "duration": "7 days"
    }
  ],
  "instructions": "Give with food after meals",
  "date": "2024-01-01T00:00:00.000Z",
  "isDelivered": false
}
```

#### 3. Create Prescription
**POST** `/api/prescriptions`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "consultationId": "consultation-uuid",
  "petId": "pet-uuid",
  "medications": [
    {
      "name": "Antibiotic",
      "dosage": "500mg",
      "frequency": "Twice daily",
      "duration": "7 days"
    },
    {
      "name": "Pain Reliever",
      "dosage": "100mg",
      "frequency": "Once daily",
      "duration": "3 days"
    }
  ],
  "instructions": "Give with food after meals. Monitor for any side effects."
}
```

**Response (201):**
```json
{
  "id": "prescription-uuid",
  "consultationId": "consultation-uuid",
  "petId": "pet-uuid",
  "vetId": "vet_001",
  "vetName": "Dr. Sarah Johnson",
  "medications": [...],
  "instructions": "Give with food after meals. Monitor for any side effects.",
  "date": "2024-01-01T00:00:00.000Z",
  "isDelivered": false,
  "createdAt": "2024-01-01T00:00:00.000Z"
}
```

#### 4. Update Delivery Status
**PUT** `/api/prescriptions/:prescriptionId/delivery`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "id": "prescription-uuid",
  "consultationId": "consultation-uuid",
  "petId": "pet-uuid",
  "vetId": "vet_001",
  "vetName": "Dr. Sarah Johnson",
  "medications": [...],
  "instructions": "...",
  "date": "2024-01-01T00:00:00.000Z",
  "isDelivered": true,
  "deliveredAt": "2024-01-02T00:00:00.000Z"
}
```

---

### ❤️ Health Check

#### 1. Health Check
**GET** `/api/health`

**No authentication required**

**Response (200):**
```json
{
  "status": "OK",
  "message": "Online Vet Hospital API is running",
  "timestamp": "2024-01-01T00:00:00.000Z"
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "error": "All fields are required"
}
```

### 401 Unauthorized
```json
{
  "error": "Invalid token"
}
```

### 403 Forbidden
```json
{
  "error": "Access denied"
}
```

### 404 Not Found
```json
{
  "error": "Pet not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "Server error",
  "details": "Error message details"
}
```

---

## Testing with Postman

1. Import the `Online_Vet_Hospital_API.postman_collection.json` file
2. Create an environment with:
   - `base_url`: `http://localhost:3000`
3. Run the "Register User" or "Login" request to get a token
4. The token will be automatically saved to the environment
5. All subsequent requests will use the token automatically

---

## Notes

- All timestamps are in ISO 8601 format
- JWT tokens expire after 7 days
- Passwords are hashed using bcrypt
- Distance is calculated in kilometers
- The API uses in-memory storage (data resets on server restart)
- For production, replace with a proper database
