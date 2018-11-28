// import * as functions from 'firebase-functions';

// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
const serviceAccount = require('../service.json');

admin.initializeApp({
	credential: admin.credential.cert(serviceAccount),
  	databaseURL: 'https://kickback-7517d.firebaseio.com'
});

// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

export const INVITE_NOTIFICATION = functions.firestore.document('profiles/{id}/invites/{invite}')
	.onCreate((snapshot, context) => {
		console.log(snapshot.data());
		return admin.messaging().send({
			data: {
				invite: snapshot.get('eventId')
			},
			notification: {
				title: 'hola',
				body: 'hallo'
			},
			token: "dCZPlnDNEQY:APA91bGyTdaAne4zT89_murqGlT6LzDu84CCotG_469FeNDXLe63acwascKlQMktrpO8VKcmx38gCOY4Q9eifN2BOeg4xSbqCaMX3OVjuJwOkeAm3nPFl8fXRjYe66i8nRNnblPJyC78"
		}).then((response) => {
			// Response is a message ID string.
			console.log('Successfully sent message:', response);
		}).catch((error) => {
			console.log('Error sending message:', error);
		});
	});
