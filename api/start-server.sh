#!/bin/bash

echo "========================================"
echo "Online Vet Hospital API Server"
echo "========================================"
echo ""

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "ERROR: Node.js is not installed"
    echo "Please install Node.js from https://nodejs.org/"
    exit 1
fi

# Check if npm is installed
if ! command -v npm &> /dev/null; then
    echo "ERROR: npm is not installed"
    echo "Please install Node.js from https://nodejs.org/"
    exit 1
fi

echo "Checking if dependencies are installed..."
if [ ! -d "node_modules" ]; then
    echo "Installing dependencies..."
    npm install
    if [ $? -ne 0 ]; then
        echo "ERROR: Failed to install dependencies"
        exit 1
    fi
    echo "Dependencies installed successfully!"
    echo ""
fi

echo "Starting server..."
echo ""
echo "Server will run on http://localhost:3000"
echo "Keep this terminal open while using the API"
echo "Press Ctrl+C to stop the server"
echo ""
echo "========================================"
echo ""

node server.js
