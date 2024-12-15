import React from 'react';

const Home = () => {
  return (
    <div className="min-h-screen bg-gray-100">
      {/* Main Container */}
      <div className="container mx-auto p-6">
        {/* Header Section */}
        <header className="text-center mb-8">
          <h1 className="text-4xl font-bold text-blue-600">Welcome to Your Note-Taking App</h1>
          <p className="mt-4 text-lg text-gray-700">Keep track of your thoughts, ideas, and important notes.</p>
        </header>

        {/* Image Section */}
        <div className="flex justify-center gap-4 mb-8">
          {/* <img
            src="https://th.bing.com/th/id/OIP.9O7nzsbMRWLBqd_U4JCBZwAAAA?w=181&h=181&c=7&r=0&o=5&dpr=1.5&pid=1.7"
            alt="Notepad"
            className="rounded-lg shadow-lg max-w-[50%] sm:max-w-xs"
          /> */}
          {/* <img
            src="https://th.bing.com/th/id/OIP.gQk2c9mtYyhI7m0Pa6_t5QHaHa?w=148&h=150&c=7&r=0&o=5&dpr=1.5&pid=1.7"
            alt="Writing"
            className="rounded-lg shadow-lg max-w-[50%] sm:max-w-xs"
          /> */}
        </div>

        {/* Features Section */}
        <section className="text-center mb-8">
          <h2 className="text-2xl font-semibold text-blue-600">Why Use Our App?</h2>
          <ul className="list-none mt-6 text-lg text-gray-700">
            <li className="mb-2">📝 Easy to organize your notes</li>
            <li className="mb-2">🔒 Secure and private</li>
            <li className="mb-2">🚀 Fast and reliable</li>
          </ul>
        </section>

        {/* Call to Action */}
        <section className="text-center">
          <h2 className="text-2xl font-semibold text-blue-600">Get Started Today</h2>
          <p className="mt-4 text-lg text-gray-700">Start creating your first note now!</p>
          <div className="mt-6">
            <a
              href="/signin"
              className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              Sign In
            </a>
          </div>
        </section>
      </div>
    </div>
  );
};

export default Home;
