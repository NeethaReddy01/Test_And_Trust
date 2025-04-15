import React from 'react';
import { Scroll, Shield, UserCheck, BadgeCheck, Scale, Heart } from 'lucide-react';

function TermsCondition() {
    return (
        <div className="bg-indigo-50 min-h-screen"> {/* Updated background here */}
            {/* Header */}
            <header className="bg-white/80 backdrop-blur-sm shadow-sm">
                <div className="max-w-7xl mx-auto px-4 py-6 sm:px-6 lg:px-8">
                    <h1 className="text-3xl font-bold text-gray-900">Terms and Conditions</h1>
                </div>
            </header>

            {/* Main Content */}
            <main className="max-w-7xl mx-auto px-4 py-8 sm:px-6 lg:px-8">
                <div className="bg-white/90 backdrop-blur-sm shadow-lg rounded-lg overflow-hidden">

                    {/* Introduction */}
                    <div className="p-6 border-b border-gray-200">
                        <div className="flex items-center gap-2 mb-4">
                            <Scroll className="w-6 h-6 text-purple-700" />
                            <h2 className="text-xl font-semibold text-gray-900">Welcome to Our Platform</h2>
                        </div>
                        <p className="text-gray-600">
                            This website provides a platform for users to explore and order sample-sized beauty and personal care products.
                            Our mission is to allow customers to try products before committing to full-size purchases.
                            We do not sell full-size products. All items listed are for trial and personal use only.
                        </p>
                    </div>

                    {/* Key Sections */}
                    <div className="p-6 space-y-8">
                        {/* Account Terms */}
                        <section>
                            <div className="flex items-center gap-2 mb-4">
                                <UserCheck className="w-6 h-6 text-purple-700" />
                                <h3 className="text-lg font-semibold text-gray-900">Account Terms</h3>
                            </div>
                            <ul className="list-disc list-inside text-gray-600 space-y-2">
                                <li>To access certain features, you may need to create an account.</li>
                                <li>You must provide accurate, complete, and current information.</li>
                                <li>You are responsible for maintaining the confidentiality of your login credentials.</li>
                                <li>You agree not to share your account or let others access it using your credentials.</li>
                                <li>We reserve the right to suspend or terminate your account for any misuse.</li>
                            </ul>
                        </section>

                        {/* Product Terms */}
                        <section>
                            <div className="flex items-center gap-2 mb-4">
                                <BadgeCheck className="w-6 h-6 text-purple-700" />
                                <h3 className="text-lg font-semibold text-gray-900">Product Terms</h3>
                            </div>
                            <ul className="list-disc list-inside text-gray-600 space-y-2">
                                <li>All items are sample-sized and provided for evaluation purposes only.</li>
                                <li>Quantities may be limited per user or per order to ensure fairness.</li>
                                <li>Samples may not be resold, repackaged, or used for commercial distribution.</li>
                                <li>Product descriptions, ingredients, and imagery are provided by manufacturers and are for reference only.</li>
                                <li>It is your responsibility to read product labels and check for allergens.</li>
                                <li>We are not liable for adverse reactions or misuse.</li>
                            </ul>
                        </section>

                        {/* Privacy & Security */}
                        <section>
                            <div className="flex items-center gap-2 mb-4">
                                <Shield className="w-6 h-6 text-purple-700" />
                                <h3 className="text-lg font-semibold text-gray-900">Privacy & Security</h3>
                            </div>
                            <ul className="list-disc list-inside text-gray-600 space-y-2">
                                <li>Your personal information is protected under our Privacy Policy.</li>
                                <li>We use industry-standard security measures to protect your data.</li>
                                <li>We do not share your information with third parties without consent.</li>
                                <li>You can request deletion of your account data at any time.</li>
                            </ul>
                        </section>

                        {/* Legal Rights */}
                        <section>
                            <div className="flex items-center gap-2 mb-4">
                                <Scale className="w-6 h-6 text-purple-700" />
                                <h3 className="text-lg font-semibold text-gray-900">Legal Rights</h3>
                            </div>
                            <ul className="list-disc list-inside text-gray-600 space-y-2">
                                <li>We reserve the right to modify these terms at any time.</li>
                                <li>Any disputes will be resolved in accordance with local laws.</li>
                                <li>You retain all rights to your personal data.</li>
                                <li>Our platform's content is protected by copyright and trademark laws.</li>
                            </ul>
                        </section>

                        {/* Acceptance */}
                        <section className="bg-indigo-100 p-6 rounded-lg">
                            <div className="flex items-center gap-2 mb-4">
                                <Heart className="w-6 h-6 text-purple-700" />
                                <h3 className="text-lg font-semibold text-gray-900">Acceptance of Terms</h3>
                            </div>
                            <p className="text-gray-700">
                                By using our platform, you acknowledge that you have read, understood, and agree to be bound by these terms and conditions.
                                If you do not agree with any part of these terms, you may not use our service.
                            </p>
                        </section>
                    </div>

                    {/* Footer */}
                    <div className="bg-white px-6 py-4 text-sm text-gray-500 mt-4">
                        <p>Last updated: {new Date().toLocaleDateString()}</p>
                        <p>For any questions regarding these terms, please contact our support team.</p>
                    </div>

                </div>
            </main>
        </div>
    );
}

export default TermsCondition;
