# Online Vet Hospital - REST API

RESTful API server for the Online Vet Hospital mobile application.

## Features

- User authentication (JWT-based)
- Pet registration and management
- Online consultations (chat/video)
- Nearby veterinary clinic finder
- Digital prescriptions management

## Setup

### Prerequisites

- Node.js (v14 or higher)
- npm or yarn

### Installation

1. Navigate to the `api` directory:
```bash
cd api
```

2. Install dependencies:
```bash
npm install
```

3. Start the server:
```bash
npm start
```

For development with auto-reload:
```bash
npm run dev
```

The server will start on `http://localhost:3000`

## API Endpoints

### Authentication

#### Register User
```
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "+1234567890"
}
```

#### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

#### Get Current User
```
GET /api/auth/me
Authorization: Bearer <token>
```

### Pets

#### Get All Pets
```
GET /api/pets
Authorization: Bearer <token>
```

#### Get Pet by ID
```
GET /api/pets/:petId
Authorization: Bearer <token>
```

#### Register Pet
```
POST /api/pets
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Buddy",
  "breed": "Golden Retriever",
  "age": 3,
  "species": "Dog",
  "medicalHistory": "No known allergies",
  "photoUri": "https://example.com/photo.jpg"
}
```

#### Update Pet
```
PUT /api/pets/:petId
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Buddy",
  "breed": "Golden Retriever",
  "age": 4
}
```

#### Delete Pet
```
DELETE /api/pets/:petId
Authorization: Bearer <token>
```

### Consultations

#### Get All Consultations
```
GET /api/consultations
Authorization: Bearer <token>
```

#### Get Consultation by ID
```
GET /api/consultations/:consultationId
Authorization: Bearer <token>
```

#### Start Consultation
```
POST /api/consultations
Authorization: Bearer <token>
Content-Type: application/json

{
  "petId": "pet-uuid",
  "type": "CHAT" // or "VIDEO_CALL"
}
```

#### End Consultation
```
PUT /api/consultations/:consultationId/end
Authorization: Bearer <token>
```

#### Send Chat Message
```
POST /api/consultations/:consultationId/messages
Authorization: Bearer <token>
Content-Type: application/json

{
  "message": "My dog has been coughing for 2 days"
}
```

#### Get Messages
```
GET /api/consultations/:consultationId/messages
Authorization: Bearer <token>
```

### Veterinary Clinics

#### Find Nearby Clinics
```
GET /api/vets/nearby?latitude=40.7128&longitude=-74.0060
Authorization: Bearer <token>
```

#### Get All Clinics
```
GET /api/vets/clinics
Authorization: Bearer <token>
```

#### Get Clinic by ID
```
GET /api/vets/clinics/:clinicId
Authorization: Bearer <token>
```

#### Get Available Veterinarians
```
GET /api/vets/doctors
Authorization: Bearer <token>
```

### Prescriptions

#### Get Prescriptions
```
GET /api/prescriptions?petId=pet-uuid
Authorization: Bearer <token>
```

#### Get Prescription by ID
```
GET /api/prescriptions/:prescriptionId
Authorization: Bearer <token>
```

#### Create Prescription
```
POST /api/prescriptions
Authorization: Bearer <token>
Content-Type: application/json

{
  "consultationId": "consultation-uuid",
  "petId": "pet-uuid",
  "medications": [
    {
      "name": "Antibiotic",
      "dosage": "500mg",
      "frequency": "Twice daily",
      "duration": "7 days"
    }
  ],
  "instructions": "Give with food"
}
```

#### Update Delivery Status
```
PUT /api/prescriptions/:prescriptionId/delivery
Authorization: Bearer <token>
```

### Health Check

#### Check API Status
```
GET /api/health
```

## Postman Collection

Import the Postman collection file (`Online_Vet_Hospital_API.postman_collection.json`) into Postman to test all endpoints.

### Using the Collection

1. Open Postman
2. Click "Import"
3. Select the `Online_Vet_Hospital_API.postman_collection.json` file
4. The collection will be imported with all endpoints pre-configured

### Environment Variables

Create a Postman environment with:
- `base_url`: `http://localhost:3000`
- `token`: (will be set automatically after login)

### Testing Flow

1. **Register/Login**: Use the register or login endpoint to get a token
2. **Set Token**: Copy the token from the response and set it in the environment variable
3. **Register Pet**: Create a pet for testing
4. **Start Consultation**: Start a consultation with the pet
5. **Send Messages**: Send chat messages
6. **Find Nearby Vets**: Search for nearby clinics
7. **Create Prescription**: Create a prescription (after consultation)

## Response Format

### Success Response
```json
{
  "id": "uuid",
  "name": "Example",
  ...
}
```

### Error Response
```json
{
  "error": "Error message"
}
```

## Authentication

All endpoints (except `/api/auth/register`, `/api/auth/login`, and `/api/health`) require authentication.

Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-token>
```

## Notes

- This is a demo API using in-memory storage
- For production, replace with a proper database (MongoDB, PostgreSQL, etc.)
- JWT secret should be changed to a secure random string
- Password hashing uses bcrypt
- All timestamps are in ISO 8601 format

## Production Considerations

- Use environment variables for configuration
- Implement rate limiting
- Add input validation and sanitization
- Use a proper database (MongoDB, PostgreSQL)
- Implement proper error logging
- Add API versioning
- Set up HTTPS
- Implement CORS properly for production domains
