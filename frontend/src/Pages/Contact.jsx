import React, { useState } from 'react';
import axios from 'axios';
import { Mail, MapPin, Globe, Phone } from 'lucide-react';

function Contact() {
  const [userInput, setUserInput] = useState('');
  const [response, setResponse] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setResponse('');
    setUserInput('');

    try {
      const res = await axios.post(
        'https://api.groq.com/openai/v1/chat/completions',
        {
          model: 'llama3-70b-8192',
          messages: [
            {
              role: 'system',
              content:
                'You are a customer care assistant for an ecommerce website called Test & Trust, which sells product samples under these categories: Haircare, Bath, Body, Hygiene, Fragrance, Makeup, Cleanser, Moisturizer, Serum, Sunscreen. Help users with their inquiries about these products or categories. If unclear, ask the user to clarify.',
            },
            {
              role: 'user',
              content: userInput,
            },
          ],
        },
        {
          headers: {
            Authorization: `Bearer ${process.env.REACT_APP_GROQ_API_KEY}`,
            'Content-Type': 'application/json',
          },
        }
      );

      const reply = res.data.choices[0].message.content.trim();
      setResponse(reply);
    } catch (err) {
      console.error(err);
      setError('Something went wrong. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-indigo-50 min-h-screen">
      <div className="min-h-screen flex flex-col items-center justify-center px-4 py-16 text-gray-800">
        <div className="text-center max-w-4xl mx-auto">
          <h2 className="text-xl tracking-wider mb-6 text-blue-600">TEST & TRUST</h2>
          <h1 className="text-6xl font-bold mb-8 text-gray-900">Contact Us</h1>
          <p className="text-xl italic mb-16 max-w-2xl mx-auto text-gray-600">
            Reach out to our dedicated team for any inquiries, assistance, or information you need.
          </p>

          {/* Contact Information */}
          <div className="space-y-8 max-w-xl mx-auto mb-12">
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

          {/* AI Chat Section */}
          <div className="bg-white shadow-lg rounded-2xl p-6 max-w-xl mx-auto w-full">
            <h3 className="text-2xl font-semibold mb-4 text-gray-800">Ask Our AI Assistant</h3>
            <form onSubmit={handleSubmit} className="space-y-4">
              <input
                type="text"
                value={userInput}
                onChange={(e) => setUserInput(e.target.value)}
                placeholder="Type your question here..."
                className="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-blue-400"
                required
              />
              <button
                type="submit"
                className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                disabled={loading}
              >
                {loading ? 'Processing...' : 'Submit'}
              </button>
            </form>

            {response && (
              <div className="mt-4 p-4 bg-green-100 rounded-lg text-green-800">
                <strong>AI Response:</strong> {response}
              </div>
            )}

            {error && (
              <div className="mt-4 p-4 bg-red-100 rounded-lg text-red-800">
                {error}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Contact;
