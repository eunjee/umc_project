    // Import the functions you need from the SDKs you need
    importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-app.js');
    importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-messaging.js');
    // TODO: Add SDKs for Firebase products that you want to use
    // https://firebase.google.com/docs/web/setup#available-libraries

    // Your web app's Firebase configuration
    // For Firebase JS SDK v7.20.0 and later, measurementId is optional
    const firebaseConfig = {
    apiKey: "AIzaSyC4-rfNJYKf0cP4H__0xSEkar0ZW9E1txg",
    authDomain: "umc-baemin.firebaseapp.com",
    projectId: "umc-baemin",
    storageBucket: "umc-baemin.appspot.com",
    messagingSenderId: "966939165631",
    appId: "1:966939165631:web:ae9a1c4e3bfc6d17bd970a",
    measurementId: "G-FC73SMRTQJ"
};

    // Initialize Firebase
    firebase.initializeApp(firebaseConfig);
    const messaging = firebase.messaging();