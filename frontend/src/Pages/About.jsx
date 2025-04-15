import React from "react";
import { ShoppingBag, Heart, Star, Users, Sparkles, Package, Globe, Clock, Shield, Gift } from "lucide-react";

const About = () => {
  return (
    <div className="bg-gradient-to-b from-[#f9f9fb] to-white min-h-screen">
      {/* Hero Section */}
      <div className="bg-indigo-50 py-16">
        <div className="max-w-4xl mx-auto text-center px-6">
          <h1 className="text-5xl font-bold mb-6 bg-gradient-to-r from-indigo-600 to-indigo-800 bg-clip-text text-transparent">
            About Us
          </h1>
          <p className="text-xl mb-8 leading-relaxed text-gray-700 max-w-3xl mx-auto">
            <strong>Test&Trust</strong> is revolutionizing the beauty and personal care industry by bridging the gap between customers and emerging brands through our innovative try-before-you-buy platform.
          </p>
        </div>
      </div>

      <div className="max-w-4xl mx-auto px-6 py-12">
        {/* Stats Section */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-12">
          {[
            { number: "50K+", label: "Happy Customers", icon: <Users className="w-6 h-6" /> },
            { number: "200+", label: "Partner Brands", icon: <Globe className="w-6 h-6" /> },
            { number: "1M+", label: "Products Sampled", icon: <Gift className="w-6 h-6" /> },
          ].map((stat, index) => (
            <div key={index} className="bg-white shadow-lg rounded-xl p-6 text-center transform hover:scale-105 transition-transform duration-300">
              <div className="flex justify-center mb-4 text-indigo-600">{stat.icon}</div>
              <h3 className="text-3xl font-bold text-gray-800 mb-2">{stat.number}</h3>
              <p className="text-gray-600">{stat.label}</p>
            </div>
          ))}
        </div>

        {/* Value Proposition */}
        <div className="bg-white shadow-lg rounded-xl p-8 mb-12 transform hover:scale-[1.02] transition-transform duration-300">
          <p className="text-lg leading-relaxed text-gray-700">
            We build trust by offering sample-sized products at affordable prices, enabling you to make confident purchasing decisions. For emerging brands, we provide a launchpad to connect with genuine users who truly value product experience and authenticity.
          </p>
        </div>

        {/* Categories Section */}
        <div className="bg-white shadow-lg rounded-xl p-8 mb-12">
          <div className="flex items-center justify-center mb-6">
            <ShoppingBag className="w-6 h-6 text-indigo-600 mr-3" />
            <h2 className="text-2xl font-bold text-gray-800">Our Categories</h2>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
            {[
              { icon: <Sparkles className="w-5 h-5" />, name: "Bath, Body & Hygiene" },
              { icon: <Star className="w-5 h-5" />, name: "Fragrances" },
              { icon: <Heart className="w-5 h-5" />, name: "Haircare" },
              { icon: <Package className="w-5 h-5" />, name: "Makeup" },
              { icon: <Users className="w-5 h-5" />, name: "Skincare" },
            ].map((category, index) => (
              <div key={index} className="flex items-center space-x-3 p-4 bg-indigo-50 rounded-lg">
                <span className="text-indigo-600">{category.icon}</span>
                <span className="text-gray-700 font-medium">{category.name}</span>
              </div>
            ))}
          </div>
        </div>

        {/* Mission Section */}
        <div className="bg-white shadow-lg rounded-xl p-8 mb-12">
          <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">Our Mission</h2>
          <div className="bg-gradient-to-r from-indigo-50 to-purple-50 p-6 rounded-lg">
            <p className="text-gray-700 leading-relaxed text-lg">
              Our mission is to revolutionize the way people discover and experience new products. We empower consumers to make informed choices while providing emerging brands the platform they deserve. We believe in a future where every product discovery is backed by first-hand experience and trust.
            </p>
          </div>
        </div>

        {/* Core Values Section */}
        <div className="bg-white shadow-lg rounded-xl p-8 mb-12">
          <h2 className="text-2xl font-bold text-gray-800 mb-8 text-center">Our Core Values</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {[
              {
                icon: <Shield className="w-6 h-6" />,
                title: "Trust",
                description: "Building confidence through transparency and authenticity in every interaction."
              },
              {
                icon: <Star className="w-6 h-6" />,
                title: "Quality",
                description: "Maintaining high standards in product curation and customer experience."
              },
              {
                icon: <Clock className="w-6 h-6" />,
                title: "Innovation",
                description: "Continuously evolving our platform to better serve our community."
              },
            ].map((value, index) => (
              <div key={index} className="text-center p-6 bg-indigo-50 rounded-xl">
                <div className="flex justify-center mb-4 text-indigo-600">{value.icon}</div>
                <h3 className="text-xl font-semibold mb-3 text-gray-800">{value.title}</h3>
                <p className="text-gray-600">{value.description}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Why Choose Us Section */}
        <div className="bg-white shadow-lg rounded-xl p-8">
          <h2 className="text-2xl font-bold text-gray-800 mb-8 text-center">Why Choose Test&Trust?</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {[
              "Experience products firsthand with convenient sample-sized deliveries",
              "Support innovative indie brands reshaping the industry",
              "Access transparent reviews and competitive pricing",
              "Explore carefully curated categories for your lifestyle",
            ].map((benefit, index) => (
              <div key={index} className="flex items-start space-x-4 p-4 bg-indigo-50 rounded-lg">
                <div className="flex-shrink-0 w-8 h-8 bg-indigo-600 rounded-full flex items-center justify-center text-white font-semibold">
                  {index + 1}
                </div>
                <p className="text-gray-700">{benefit}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default About;