# Smart Note Management App

<!-- Description -->

A full-stack application for managing notes, built with **Spring Boot** and **React**.

## Features

- Create, Read, update, and delete notes.
- Organize notes efficiently.
- JWT token based authorization
- Super Admin and Admin user to manager users (Create, Update, Read, Delete, Activate and Lock)
- User-friendly interface with responsive design.
- Protected routes for different users(Frontend).

## Tech Stack

- **Backend:** Spring Boot (Java)
- **Frontend:** React + Vite (JavaScript)
- **Database:** Postgresql for prod environment, Mysql for Dev environment
- **Styling:** Tailwind Css

## Installation

### Backend Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/KaushalPrajapat/notes-full-stack-app.git
   ```
2. Move to backend directory
   ```bash
   cd notes-app-back-end
   ```
3. Run mvn command
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup

1. Move to frontend directory
   ```bash
   cd notes-app-front-end
   ```
2. install dependency
   ```bash
   npm install
   ```
3. Run file (default port is 3000)
   ```bash
   npm run dev
   ```

#### Api Test (Images)

Complete postman api config is

- Api Structure ![Api Structure](assets/images/api_structure.png)
- Signin ![Signin](assets/images/signin.png)
- Signup ![Signup](assets/images/signup.png)
- validate-user ![validate-user](assets/images/validate-user.png)
- Get logged in user ![Get logged in user](assets/images/user.png)
- Download Other Api and test: [Postman Collection](assets/zip/NOTEAPP_POSTMAN.zip)

#### Frontend (Images)

##### Public

- Home Page ![Home Page](assets/images/frontend/basic/home.png)
- About Section ![About Section](assets/images/frontend/basic/aboutme.png)
- Signin ![Signin](assets/images/frontend/basic/signin.png)
- Signup ![Signup](assets/images/frontend/basic/signup.png)

##### User

- All Notes ![All Notes](assets/images/frontend/user/notes.png)
- Add Note ![Add Note](assets/images/frontend/user/addnote.png)
- Delete a Note ![Delete a Note](assets/images/frontend/user/deletenote.png)
- Edit a Note ![Edit a Note](assets/images/frontend/user/editnote.png)
- Changelog of a Note ![Note Change log](assets/images/frontend/user/notechangelog.png)

##### Admin

- Admin Home ![Admin Home](assets/images/frontend/admin/admin_home.png)
- Admin profile ![Admin Profile](assets/images/frontend/admin/admin_profile.png)
- All Users Operations ![All Users](assets/images/frontend/Admin/all_user.png)

##### Super Admin

- Super Admin Home ![Super Admin Home](assets/images/frontend/admin/su_home.png)
- Super Admin profile ![Super Admin Profile](assets/images/frontend/admin/su_profile.png)
- Users all Logs ![All users all logs](assets/images/frontend/Admin/su_all_logs_all_users.png)