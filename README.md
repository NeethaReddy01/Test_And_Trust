# 🛍️ Test & Trust - Sample First E-Commerce Platform

**Test & Trust** is a full-stack e-commerce web application that allows users to try sample-sized products before purchasing the original full-size items. It’s designed to enhance customer confidence and trust in products through a "try-before-you-buy" experience.

## 📌 Features

### 👤 Customer Module
- 🛒 Browse and search products by categories (Skincare, Haircare, Fragrance, Bath & Body, Hygiene)
- 🔍 View detailed product descriptions and images
- 🧺 Add products to cart and checkout
- 💳 Secure payment integration using Razorpay (Test Mode)
- 📦 Track orders and view order history
- 🔐 JWT-based authentication for secure login and registration

### 🛠️ Admin Module
- 📦 Add, update, or delete products with categories
- 👥 View all registered users
- 📊 Manage and update order statuses
- 📈 View detailed sales analytics:
    📅 Weekly, Monthly, and Yearly revenue and order statistics

## 🧑‍💻 Tech Stack

### Frontend
- React.js
- Axios
- React Router
- Bootstrap / Tailwind CSS (or mention the UI framework used)

### Backend
- Spring Boot (Java)
- RESTful APIs
- Spring Security with JWT

### Database
- MySQL

### Payment Gateway
- Razorpay API (Test Mode)

## 🔧 Project Architecture

React (Frontend)
|
Spring Boot (Backend - APIs & Business Logic)
|
MySQL (Database)
|
Razorpay (Payment Integration)



## 🚀 Installation & Setup

### Backend (Spring Boot)
```bash
1. Clone the repository
2. Navigate to backend directory
3. Configure `application.properties` with your MySQL credentials and Razorpay keys
4. Run the application using your IDE or `./mvnw spring-boot:run`


### Frontend (React)
1. Navigate to the frontend directory
2. Run `npm install`
3. Run `npm start` to start the development server


## Folder Structure
/frontend - React frontend code
/backend  - Spring Boot backend code



✍️ Authors
Shiva Ganesh
Krishna Kaushal
Nanda Kumar
Neetha
Ritvika


Note: This project is for learning/demo purposes and Razorpay is integrated in test mode only.
