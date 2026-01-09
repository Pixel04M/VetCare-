# API Setup Instructions - Quick Start

## Prerequisites

Make sure you have **Node.js** installed on your computer.

1. Check if Node.js is installed:
   ```bash
   node --version
   npm --version
   ```

2. If not installed, download from: https://nodejs.org/

## Step-by-Step Setup

### Step 1: Open Terminal/Command Prompt

- **Windows**: Press `Win + R`, type `cmd` or `powershell`, press Enter
- **Mac/Linux**: Open Terminal

### Step 2: Navigate to API Directory

```bash
cd "D:\NEW APP\api"
```

**Note**: Adjust the path if your project is in a different location.

### Step 3: Install Dependencies

```bash
npm install
```

This will install all required packages (express, cors, jsonwebtoken, etc.)

**Expected output:**
```
added 150 packages in 30s
```

### Step 4: Start the Server

```bash
npm start
```

**Expected output:**
```
🚀 Online Vet Hospital API server running on port 3000
📝 API Documentation: http://localhost:3000/api/health

Available endpoints:
  POST   /api/auth/register
  POST   /api/auth/login
  GET    /api/auth/me
  GET    /api/pets
  POST   /api/pets
  GET    /api/consultations
  POST   /api/consultations
  GET    /api/vets/nearby
  GET    /api/prescriptions
```

### Step 5: Keep the Terminal Open

**IMPORTANT**: Keep the terminal window open while using the API. The server must be running for Postman requests to work.

### Step 6: Test in Postman

1. Open Postman
2. Make sure the server is running (you should see the message above)
3. Try the "Health Check" endpoint first:
   - GET `http://localhost:3000/api/health`
4. Then try "Register User" or "Login"

## Troubleshooting

### Error: "npm is not recognized"

**Solution**: Node.js is not installed or not in PATH.
1. Install Node.js from https://nodejs.org/
2. Restart your terminal/command prompt
3. Try again

### Error: "Cannot find module"

**Solution**: Dependencies not installed.
```bash
cd "D:\NEW APP\api"
npm install
```

### Error: "Port 3000 already in use"

**Solution**: Another application is using port 3000.
1. Find and close the application using port 3000, OR
2. Change the port in `server.js`:
   ```javascript
   const PORT = process.env.PORT || 3001; // Change to 3001 or another port
   ```
3. Update Postman base_url to match

### Error: "ECONNREFUSED 127.0.0.1:3000"

**Solution**: Server is not running.
1. Make sure you completed Step 4 (npm start)
2. Check that the terminal shows "server running on port 3000"
3. Keep the terminal window open

### Server stops unexpectedly

**Solution**: Use nodemon for auto-restart (optional):
```bash
npm run dev
```

## Quick Test Commands

### Test 1: Health Check (No authentication needed)
```bash
curl http://localhost:3000/api/health
```

Or in browser: http://localhost:3000/api/health

### Test 2: Register User (using curl)
```bash
curl -X POST http://localhost:3000/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Test User\",\"email\":\"test@example.com\",\"password\":\"test123\",\"phone\":\"+1234567890\"}"
```

## Alternative: Using PowerShell

If you're using PowerShell on Windows:

```powershell
# Navigate to API directory
cd "D:\NEW APP\api"

# Install dependencies
npm install

# Start server
npm start
```

## Verification Checklist

- [ ] Node.js is installed (`node --version` works)
- [ ] npm is installed (`npm --version` works)
- [ ] Navigated to `D:\NEW APP\api` directory
- [ ] Ran `npm install` successfully
- [ ] Ran `npm start` and see "server running on port 3000"
- [ ] Terminal window is still open
- [ ] Can access http://localhost:3000/api/health in browser
- [ ] Postman requests work

## Need Help?

If you're still having issues:
1. Check that Node.js is installed: `node --version`
2. Make sure you're in the correct directory: `cd "D:\NEW APP\api"`
3. Verify server.js file exists in the api folder
4. Check the terminal for any error messages
