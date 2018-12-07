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
			token: "dZ4p9SxtotU:APA91bH-D6kqKADa_Vfbza7r_SF0kivVZpqwdob5w3qw9uAaVckTq6IpnvovIQz4QrOVoKxjIN7O-OSkJr5dJYD4s56r8eV_fKJWS0_Sf91mrDrSBfdOpphAQvDizQu2Oc0CrkShlIH0"
		}).then((response) => {
			// Response is a message ID string.
			console.log('Successfully sent message:', response);
		}).catch((error) => {
			console.log('Error sending message:', error);
		});
	});
