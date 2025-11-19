<img width="1898" height="830" alt="Screenshot 2025-11-19 092258" src="https://github.com/user-attachments/assets/63244bda-0916-48dd-b660-e11c1f0d4141" />
📦 CloudShare – Backend (Spring Boot + MongoDB)

CloudShare is a cloud-based file sharing platform that allows users to upload, manage, organize and share files securely.
This repository contains the complete backend built with Spring Boot, MongoDB, and Clerk authentication.

🚀 Features
🔐 Authentication
Clerk authentication (JWT verified using JWKS)
Automatic user sync using Clerk webhooks
Protected routes via custom JWT filter
Stateless authentication (no sessions)

📁 File Management
Upload multiple files
Stream-based storage for handling large files
Download & preview files
Public & private file sharing
Toggle file visibility
Delete files
Folder-wise organization on frontend

💳 Credits System
Each file upload consumes credits
Auto credit creation for new users
Credit validation before upload
Track remaining credits

📦 Storage Handling
Local filesystem storage under /uploads
Secure access layer for downloaded files
Metadata stored in MongoDB (name, type, size, timestamp, visibility)

🔔 Webhooks
Handles user.created, user.updated, user.deleted
Auto updates profile information
Creates initial credits for new users

🛠️ Tech Stack
Spring Boot 3
MongoDB
Clerk Authentication + Webhooks
Spring Security (Custom JWT Filter)
Lombok
Maven
Local File Storage


🧱 Project Structure
src/main/java/com/umangcraft/cloudshare
│
├── config/                 # Security & CORS configuration
├── controller/             # REST Controllers
├── documents/              # MongoDB documents
├── dto/                    # Data Transfer Objects
├── exceptions/             # Global exception handler
├── repository/             # Mongo repositories
├── security/               # JWT validation + JWKS resolver
├── service/                # Business logic layer
└── uploads/                # File storage directory

🔐 Authentication Flow (Clerk)

Frontend sends a Clerk JWT
Backend → extracts kid from header
Backend → fetches public key from Clerk JWKS
Backend → verifies JWT using RSA public key
On success → injects Clerk User ID into SecurityContext
API executes with authenticated user

📁 File Upload Flow
User uploads files
Backend checks available credits
Files stored in /uploads using streaming
Metadata saved in MongoDB
Credits deducted
Returns uploaded file list + remaining credits

API Endpoints
Files
Method	Endpoint	Description
POST	/files/upload	Upload files
GET	/files/my	Get logged-in user files
GET	/files/public/{id}	Get public file metadata
GET	/files/download/{id}	Download a file
DELETE	/files/{id}	Delete file
PATCH	/files/{id}/toggle-public	Toggle visibility

Users
Method	Endpoint	Description
GET	/users/credits	Get user credits

⚙️ Environment Variables
Create a .env or set variables:
spring.data.mongodb.uri=
clerk.issuer=
clerk.jwks-url=
clerk.webhook.secret=

🤝 Contributions
PRs are welcome!
Open an issue if you want a new feature or find a problem.

⭐ Support the project
If you like this project, don’t forget to star ⭐ the repository!
<img width="1906" height="826" alt="Screenshot 2025-11-19 092115" src="https://github.com/user-attachments/assets/13d3183d-34a4-488c-ab80-44849ff6ad0a" />
<img width="1893" height="820" alt="Screenshot 2025-11-19 092317" src="https://github.com/user-attachments/assets/9359f6b1-c24b-4bb3-88c0-d9675a3486eb" />
<img width="1900" height="826" alt="Screenshot 2025-11-19 092331" src="https://github.com/user-attachments/assets/2ddde1aa-b107-4c61-98d9-40803f74c14a" />
<img width="1890" height="828" alt="Screenshot 2025-11-19 092405" src="https://github.com/user-attachments/assets/bad3d0a0-f26b-43e4-9542-6a1662e9276a" />
<img width="1897" height="835" alt="Screenshot 2025-11-19 094647" src="https://github.com/user-attachments/assets/83f89d85-ea75-47ec-9765-2c0d3cda466b" />
<img width="1919" height="830" alt="Screenshot 2025-11-19 094708" src="https://github.com/user-attachments/assets/4d390692-3a14-4ba0-9520-1118b5924d05" />
<img width="1919" height="840" alt="Screenshot 2025-11-19 094723" src="https://github.com/user-attachments/assets/933e40e2-a15f-4f2f-aca8-bc64bab1a564" />
