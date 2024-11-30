// src/App.jsx or src/App.js
import React, { useState } from 'react';
import '@fortawesome/fontawesome-free/css/all.min.css';
import { BrowserRouter as Router, Route, Routes, Link, Navigate } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';

import Header from './components/basic/Header';
import AddNote from './components/note/AddNote';
import AllNotes from './components/note/AllNotes';
import Profile from './components/user/Profile';
import SignIn from './components/auth/SignIn';
import Home from './components/basic/Home';
import SignUp from './components/auth/SignUp';
import Footer from './components/basic/Footer';
import ProtectedRoutes from './assets/ProtectedRoutes';
import EditNote from './components/note/EditNote';
import NoteLogs from './components/note/NoteLogs';
import ProtectedAdmin from './assets/ProtectedAdmin';
import AdminHome from './components/admin/AdminHome';
import AllUsers from './components/admin/AllUsers';
import AddUser from './components/admin/AddUser';
import UserLogs from './components/admin/UserLogs';
import AllNotesAllLogs from './components/admin/AllNotesAllLogs';
import ForgotPassword from './components/auth/ForgotPassword';

const App = () => {
  const [isSigned, setIsSigned] = useState(localStorage.getItem("signedIn"));
  const [userRole, setUserRole] = useState(localStorage.getItem("role"));
  const [isSuperUser, setIsSuperUser] = useState(localStorage.getItem("role") == "ROLE_SU");

  // console.log(userRole);
  const handleSignOut = () => {
    localStorage.removeItem("signedIn")
    localStorage.removeItem("accessToken")
    localStorage.removeItem("refreshToken")
    localStorage.removeItem("username")
    localStorage.removeItem("role")
    localStorage.removeItem("signOutTime")
    console.log("Signing out");
    setIsSigned(false);
    location.reload();
  }
  const signMeIn = () => {
    setIsSigned(true)
  }


  return (
    <Router>
      <Header isSigned={isSigned} handleSignOut={handleSignOut} userRole={userRole} />
      <div>
        {/* Navigation bar */}

        {/* Routes */}
        <Routes>
          {/* Adding public routes */}


          {!isSigned ?

            <>
              <Route path="/signin" element={<SignIn />} isSigned={isSigned} signMeIn={signMeIn} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/" element={<Home />} />
              <Route path="/*" element={<Navigate to="/" />} />
              <Route path="/forgot-password*" element={<ForgotPassword />} />


            </>
            :
            <>

              {userRole == "ROLE_USER" || userRole == "ROLE_GUEST" ?
                <>
                  {/* User Routes */}
                  <Route path='/' element={<ProtectedRoutes isSigned={isSigned} userRole={userRole} />}>
                    <Route path="user/add-note" element={<AddNote />} />
                    <Route path="" element={<AllNotes />} />
                    <Route path="user/notes" element={<AllNotes />} />
                    <Route path="user/all-notes" element={<AllNotes />} />
                    <Route path="user/profile" element={<Profile handleSignOut={handleSignOut} />} />
                    <Route path="user/edit-note/:noteId" element={<EditNote />} />
                    <Route path="user/logs-note/:noteId" element={<NoteLogs />} />
                    <Route path="*" element={<Navigate to={"/user/all-notes"} />} />
                  </Route>
                </>
                :
                <>
                  {/* Admin Routes */}
                  <Route path='/' element={<ProtectedAdmin isSigned={isSigned} userRole={userRole} />}>
                    <Route path="admin/home" element={<AdminHome isSuperUser={isSuperUser} />} />
                    <Route path="admin/notes" element={<AllNotes />} />
                    <Route path="admin/add-user" element={<AddUser />} />
                    <Route path="admin/all-users" element={<AllUsers />} />
                    {isSuperUser &&
                      <Route path="admin/su/user-logs" element={<UserLogs />} />
                    }
                    <Route path="admin/all-notes" element={<AllNotes />} />
                    <Route path="admin/all-notes-logs" element={<AllNotesAllLogs />} />
                    <Route path="admin/profile" element={<Profile handleSignOut={handleSignOut} />} />
                    <Route path="admin/edit-note/:noteId" element={<EditNote />} />
                    <Route path="admin/logs-note/:noteId" element={<NoteLogs />} />
                    <Route path="*" element={<Navigate to={"/admin/home"} />} />
                  </Route>
                </>}
            </>

          }

        </Routes>
        <Footer />
      </div>
    </Router>
  );
};

export default App;
