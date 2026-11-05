document.addEventListener('DOMContentLoaded', () => {
    // 1. Create the widget container
    const widgetContainer = document.createElement('div');
    widgetContainer.id = 'lumina-chat-widget';

    // 2. Set the HTML
    widgetContainer.innerHTML = `
        <button class="chat-toggle-btn" id="chatToggleBtn">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2v10z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
        </button>

        <div class="chat-window hidden" id="chatWindow">
            <div class="chat-header">
                <h4>Lumina Concierge</h4>
                <button id="lumina-chat-close">&times;</button>
            </div>
            <div class="chat-messages" id="chatMessages">
                <!-- Messages will be injected here by JS -->
            </div>
            <div class="chat-input-area">
                <input type="text" id="chatInput" placeholder="Type your message..." />
                <button id="chatSendBtn">Send</button>
            </div>
        </div>
    `;

    document.body.appendChild(widgetContainer);

    // 3. Select elements
    const toggleBtn = document.getElementById('chatToggleBtn');
    const chatWindow = document.getElementById('chatWindow');
    const closeBtn = document.getElementById('lumina-chat-close');
    const sendBtn = document.getElementById('chatSendBtn');
    const chatInput = document.getElementById('chatInput');
    const chatMessages = document.getElementById('chatMessages');

    // 4. Toggle visibility
    toggleBtn.addEventListener('click', () => {
        chatWindow.classList.toggle('hidden');
        // Start conversation if opened for the first time
        if (!chatWindow.classList.contains('hidden') && chatMessages.children.length === 0) {
            startConversation();
        }
    });

    closeBtn.addEventListener('click', () => {
        chatWindow.classList.add('hidden');
    });

    // 5. Build message function
    const addMessage = (text, sender, quickReplies = []) => {
        const msgDiv = document.createElement('div');
        msgDiv.classList.add('chat-msg', sender === 'user' ? 'user-msg' : 'bot-msg');
        msgDiv.textContent = text;
        chatMessages.appendChild(msgDiv);

        if (quickReplies.length > 0 && sender === 'bot') {
            const qrContainer = document.createElement('div');
            qrContainer.classList.add('quick-replies');
            quickReplies.forEach(qrText => {
                const btn = document.createElement('button');
                btn.classList.add('quick-reply-btn');
                btn.textContent = qrText;
                btn.addEventListener('click', () => {
                    qrContainer.remove();
                    handleUserInput(qrText);
                });
                qrContainer.appendChild(btn);
            });
            chatMessages.appendChild(qrContainer);
        }

        chatMessages.scrollTop = chatMessages.scrollHeight;
    };

    const addTypingIndicator = () => {
        const typingIndicator = document.createElement('div');
        typingIndicator.id = 'typingIndicator';
        typingIndicator.classList.add('typing-indicator', 'bot-msg', 'chat-msg');
        typingIndicator.innerHTML = '<div class="typing-dot"></div><div class="typing-dot"></div><div class="typing-dot"></div>';
        chatMessages.appendChild(typingIndicator);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    };

    const removeTypingIndicator = () => {
        const typingIndicator = document.getElementById('typingIndicator');
        if (typingIndicator) {
            typingIndicator.remove();
        }
    };

    // Conversation Logic
    const startConversation = () => {
        addTypingIndicator();
        setTimeout(() => {
            removeTypingIndicator();
            const welcomeMsg = "Hello! I'm the Lumina Concierge. How can I assist you in planning your extraordinary event?";
            addMessage(welcomeMsg, 'bot', ["Plan an event", "Ask a question"]);
        }, 800);
    };

    const handleUserInput = (text) => {
        addMessage(text, 'user');
        chatInput.value = '';

        // Remove quick replies when user answers
        const existingQR = chatMessages.querySelector('.quick-replies');
        if (existingQR) existingQR.remove();

        addTypingIndicator();

        setTimeout(() => {
            removeTypingIndicator();
            
            const lowerText = text.toLowerCase();
            let response = "Thank you for reaching out to Lumina Events. For specialized inquiries, please email us directly at inquire@luminaevents.com or leave your contact details here.";
            let options = ["Contact Info", "Our Services"];

            if (lowerText.includes("plan") || lowerText.includes("event") || lowerText.includes("party") || lowerText.includes("organize") || lowerText.includes("start")) {
                response = "Wonderful! To begin curating your event, please let me know what occasion you are looking to host.";
                options = ["Gala", "Corporate Event", "Private Gathering"];
            } else if (lowerText.includes("gala")) {
                response = "A Gala requires exquisite attention to detail. We recommend booking 9 to 12 months in advance. Would you like to schedule an initial consultation with our team?";
                options = ["Book Consultation", "Pricing"];
            } else if (lowerText.includes("corporate") || lowerText.includes("business")) {
                response = "Corporate events with Lumina emphasize innovation and precision. From intimate retreats to large-scale conferences, we manage every detail. Shall we schedule a call?";
                options = ["Contact Us", "How we work"];
            } else if (lowerText.includes("private") || lowerText.includes("dinner") || lowerText.includes("estate") || lowerText.includes("gathering")) {
                response = "For exclusive private gatherings, we typically require 3 to 6 months of planning to secure premium venues and vendors. Can we reach out to you with more information?";
                options = ["Contact Info", "Pricing Process"];
            } else if (lowerText.includes("price") || lowerText.includes("cost") || lowerText.includes("fee") || lowerText.includes("budget") || lowerText.includes("pricing")) {
                response = "Our fees are highly customized based on the specific scope and scale of your event. After our initial consultation, we provide a detailed, completely transparent proposal tailored to you.";
                options = ["Book Consultation"];
            } else if (lowerText.includes("advance") || lowerText.includes("time") || lowerText.includes("when") || lowerText.includes("how long")) {
                response = "For premium events and galas, we recommend booking 9 to 12 months in advance. For intimate private gatherings, 3 to 6 months is generally sufficient to plan a flawless experience.";
            } else if (lowerText.includes("international") || lowerText.includes("destination") || lowerText.includes("global") || lowerText.includes("abroad") || lowerText.includes("where")) {
                response = "Yes, Lumina orchestrates events globally! We have extensive experience managing complex logistics and bespoke experiences in major luxury destinations worldwide.";
            } else if (lowerText.includes("vendor") || lowerText.includes("partner") || lowerText.includes("caterer") || lowerText.includes("use our")) {
                response = "We maintain a curated list of preferred premium partners. However, we are happy to collaborate with specific vendors you select, provided they meet our high standards for excellence.";
            } else if (lowerText.includes("process") || lowerText.includes("how it works") || lowerText.includes("steps") || lowerText.includes("how do you work") || lowerText.includes("how we work")) {
                response = "Our process is meticulous and spans four phases: 1. Consultation, 2. Design & Planning, 3. Coordination, and 4. Execution. This ensures your unique vision becomes reality exactly as intended.";
                options = ["Book Consultation"];
            } else if (lowerText.includes("question") || lowerText.includes("hi") || lowerText.includes("hello") || lowerText.includes("hey")) {
                response = "Hello! I am the Lumina Concierge. I can help answer questions about our event services, pricing structure, availability, or instantly connect you with an event specialist.";
                options = ["Plan an event", "Pricing", "Contact Us"];
            } else if (lowerText.includes("contact") || lowerText.includes("phone") || lowerText.includes("email") || lowerText.includes("reach") || lowerText.includes("call") || lowerText.includes("book consultation")) {
                response = "You can reach our team at inquire@luminaevents.com or by calling our Beverly Hills office directly at +1 (234) 567-890. A concierge is ready to assist you.";
                options = [];
            } else if (lowerText.includes("services")) {
                response = "We specialize in three main areas: premium Galas, cutting-edge Corporate Events, and Exclusive Private Gatherings. What type of experience are you envisioning?";
                options = ["Gala", "Corporate", "Private"];
            } else if (lowerText.includes("thank you") || lowerText.includes("thanks")) {
                response = "You are very welcome! If there is anything else you need to discuss, I am here.";
                options = [];
            }

            addMessage(response, 'bot', options);
        }, 800);
    };

    // 6. Handle send
    sendBtn.addEventListener('click', () => {
        const text = chatInput.value.trim();
        if (text) handleUserInput(text);
    });

    chatInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            const text = chatInput.value.trim();
            if (text) handleUserInput(text);
        }
    });
});
