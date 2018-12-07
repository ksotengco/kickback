// import * as functions from 'firebase-functions';

// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

export const INVITE_NOTIFICATION = functions.firestore.document('profiles/{id}/invites/{invite}')
	.onCreate((snapshot, context) => {
		console.log(snapshot.data());
		return null;
	});
