# Online Vet Hospital - Mobile Application

A comprehensive mobile application that enables pet owners to register their animals and access online veterinary consultations. The app allows users to connect with certified veterinary doctors for medical advice and treatment suggestions, and provides GPS-based detection of nearby veterinary clinics.

## Features

### 🐾 Core Functionality

1. **User Authentication**
   - User registration and login
   - Secure user session management

2. **Pet Registration**
   - Register pets with photos, breed, age, and medical history
   - Support for multiple pets per user
   - View and manage registered pets

3. **Online Consultations**
   - 24/7 text chat with certified veterinary doctors
   - Video call consultations
   - Consultation history tracking

4. **GPS-Based Vet Finder**
   - Find nearby veterinary clinics using GPS
   - View clinic details (address, phone, rating)
   - Emergency clinic identification
   - Distance calculation

5. **Digital Prescriptions**
   - View prescriptions from consultations
   - Medication details and instructions
   - Order medicines with delivery option

6. **Medical History Storage**
   - Secure local storage using Room database
   - Complete medical records for each pet

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Database
- **Navigation**: Navigation Compose
- **Maps**: Google Maps Compose
- **Image Loading**: Coil
- **Permissions**: Accompanist Permissions

## Setup Instructions

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 24 or higher
- Kotlin 2.0.21 or later

### Installation

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Add your Google Maps API key:
   - Get an API key from [Google Cloud Console](https://console.cloud.google.com/google/maps-apis)
   - Uncomment the meta-data tag in `AndroidManifest.xml`
   - Replace `YOUR_GOOGLE_MAPS_API_KEY` with your actual API key

### Running the App

1. Connect an Android device or start an emulator
2. Click "Run" in Android Studio
3. The app will build and install on your device

## App Structure

```
app/src/main/java/com/example/myapplication/
├── data/
│   ├── User.kt
│   ├── Pet.kt
│   ├── Consultation.kt
│   ├── Prescription.kt
│   ├── VetClinic.kt
│   └── database/
│       ├── PetEntity.kt
│       ├── ConsultationEntity.kt
│       ├── PrescriptionEntity.kt
│       ├── PetDao.kt
│       ├── ConsultationDao.kt
│       ├── PrescriptionDao.kt
│       └── VetHospitalDatabase.kt
├── viewmodel/
│   ├── AuthViewModel.kt
│   ├── PetViewModel.kt
│   ├── ConsultationViewModel.kt
│   ├── VetClinicViewModel.kt
│   └── PrescriptionViewModel.kt
├── ui/
│   ├── screens/
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── PetRegistrationScreen.kt
│   │   ├── MyPetsScreen.kt
│   │   ├── ConsultationsScreen.kt
│   │   ├── NearbyVetsScreen.kt
│   │   └── PrescriptionsScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── navigation/
│   └── NavGraph.kt
└── MainActivity.kt
```

## Color Scheme

The app uses a modern veterinary-themed color palette:
- **Primary**: Turquoise (#2E9B94)
- **Secondary**: Coral (#E55555)
- **Tertiary**: Mint (#7FD4B0)

## Permissions

The app requires the following permissions:
- Internet (for API calls)
- Location (for finding nearby vets)
- Camera (for pet photos)
- Storage (for saving images)

## Future Enhancements

- Real-time video calling integration
- Push notifications for consultations
- Payment integration for consultations
- Medicine delivery tracking
- Multi-language support
- Cloud backup for medical records
- Appointment scheduling
- Vet rating and reviews

## Notes

- The current implementation uses mock data for veterinary clinics
- Authentication is simplified for demo purposes (should be replaced with proper backend authentication)
- Video calling requires additional integration with services like WebRTC or Twilio
- Medicine ordering requires integration with pharmacy APIs

## License

This project is created for demonstration purposes.

## Contact

For questions or support, please contact the development team.

---

**Slogan**: "Your pet's doctor is just one tap away" 🐾
