// import * as functions from 'firebase-functions';

// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

export const profileWritten = functions.firestore.document('profiles/{id}/invites/{invite}')
	.onUpdate((snapshot, context) => {
		return snapshot.ref.set({ id: 'cloud function works'}, { merge: true});
	});
