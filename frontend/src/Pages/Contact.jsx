import React from 'react';
import { Mail, MapPin, Globe, Phone } from 'lucide-react';

function Contact() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-blue-100">
      {/* Content */}
      <div className="min-h-screen flex flex-col items-center justify-center px-4 py-16 text-gray-800">
        <div className="text-center max-w-4xl mx-auto">
          {/* Company Name */}
          <h2 className="text-xl tracking-wider mb-6 text-blue-600">TEST & TRUST</h2>

          {/* Main Heading */}
          <h1 className="text-6xl font-bold mb-8 text-gray-900">Contact Us</h1>

          {/* Subheading */}
          <p className="text-xl italic mb-16 max-w-2xl mx-auto text-gray-600">
            Reach out to our dedicated team for any inquiries, assistance, or information you need.
          </p>

          {/* Contact Information */}
          <div className="space-y-8 max-w-xl mx-auto">
            <div className="flex items-center justify-center space-x-3">
              <Mail className="w-6 h-6 text-blue-600" />
              <a href="mailto:hello@reallygreatsite.com" className="hover:text-blue-600 transition-colors">
                hello@reallygreatsite.com
              </a>
            </div>

            <div className="flex items-center justify-center space-x-3">
              <MapPin className="w-6 h-6 text-blue-600" />
              <span>123 Anywhere St., Any City</span>
            </div>

            <div className="flex items-center justify-center space-x-3">
              <Globe className="w-6 h-6 text-blue-600" />
              <a href="https://www.reallygreatsite.com" className="hover:text-blue-600 transition-colors">
                www.reallygreatsite.com
              </a>
            </div>

            <div className="flex items-center justify-center space-x-3">
              <Phone className="w-6 h-6 text-blue-600" />
              <a href="tel:+1234567890" className="hover:text-blue-600 transition-colors">
                +123-456-7890
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Contact;