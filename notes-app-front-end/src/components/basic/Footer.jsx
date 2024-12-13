import React from 'react';

const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white py-10">
      <a href="how2delete.zip">delete</a>
      <div className="max-w-7xl mx-auto px-4">
        {/* Social Media Links */}
        <div className="flex justify-center space-x-6 mb-8">
          <a
            href="https://www.linkedin.com"
            target="_blank"
            rel="noopener noreferrer"
            className="text-2xl hover:text-blue-500 transition"
          >
            <i className="fab fa-linkedin"></i> {/* Using FontAwesome for social icons */}
          </a>
          <a
            href="https://github.com"
            target="_blank"
            rel="noopener noreferrer"
            className="text-2xl hover:text-gray-400 transition"
          >
            <i className="fab fa-github"></i>
          </a>
          <a
            href="https://twitter.com"
            target="_blank"
            rel="noopener noreferrer"
            className="text-2xl hover:text-blue-400 transition"
          >
            <i className="fab fa-twitter"></i>
          </a>
        </div>

        {/* About Me Section */}
        <div className="text-center mb-8">
          <h2 className="text-3xl font-bold mb-2">About Me</h2>
          <p className="text-lg">
            I'm a passionate web developer with experience in building full-stack applications. I enjoy creating intuitive and user-friendly interfaces using React, JavaScript, and other modern technologies.
          </p>
        </div>

        {/* Contact Me Section */}
        <div className="text-center">
          <h2 className="text-3xl font-bold mb-2">Contact Me</h2>
          <p className="text-lg">
            Feel free to reach out to me via{' '}
            <a href="mailto:youremail@example.com" className="text-blue-400 hover:text-blue-600 transition">
              email
            </a>{' '}
            or through my <a href="/contact" className="text-blue-400 hover:text-blue-600 transition">contact form</a>.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
