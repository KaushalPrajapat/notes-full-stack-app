import React from 'react';

const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white py-10">
      <div className="max-w-7xl mx-auto px-4">
        {/* Social Media Links */}
        <div className="flex justify-center space-x-6 mb-8">
          <a
            href="https://www.linkedin.com/in/kaushal-prajapat-b86b231a9/"
            target="_blank"
            rel="noopener noreferrer"
            className="text-2xl hover:text-blue-500 transition"
          >
            <i className="fab fa-linkedin"></i> {/* Using FontAwesome for social icons */}
          </a>
          <a
            href="https://github.com/KaushalPrajapat"
            target="_blank"
            rel="noopener noreferrer"
            className="text-2xl hover:text-gray-400 transition"
          >
            <i className="fab fa-github"></i>
          </a>
          <a
            href="https://leetcode.com/u/prajapatkaushal555/"
            target="_blank"
            rel="noopener noreferrer"
            className="text-2xl hover:text-blue-400 transition"
          >
            <i class="fa-brands fa-connectdevelop"></i>
          </a>
        </div>

        {/* About Me Section */}
        <div className="text-center mb-8">
          <h2 className="text-3xl font-bold mb-2">About Me</h2>
          <p className="text-lg">
            I’m an experienced MDG and ABAP Consultant with expertise in backend development using
            Java and microservices. I’ve worked extensively with SAP ABAP and Spring Boot, focusing on
            JWT authentication and scalable application design. In addition to enhancing performance on
            critical projects, I’ve developed practical, user-focused solutions like a notes management
            app using React and Tailwind CSS. My approach combines clean, efficient code with solving complex
            challenges to deliver high-performance results.
            For a detailed overview of my experience and projects, download my
            <a className='text-green-400'  href="https://drive.google.com/file/d/1xwwP_wd4KqjVLJ_eLVmZfTMpXnmRZd1f/view?usp=sharing"> RESUME</a>  here.</p>
        </div>

        {/* Contact Me Section */}
        <div className="text-center">
          <h2 className="text-3xl font-bold mb-2">Contact Me</h2>
          <p className="text-lg">
            Feel free to reach out to me via{' '}
            <a href="mailto:prajapatkaushal555@gmail.com" className="text-blue-400 hover:text-blue-600 transition">
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
