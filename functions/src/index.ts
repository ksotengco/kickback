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

export const INVITE_NOTIFICATION = functions.firestore.document('profiles/{profile_id}/invites/{event_id}')
	.onCreate((snapshot, context) => {
		const profileId = context.params.profile_id;
		const eventId = context.params.event_id;
		const profileRef = snapshot.ref.parent.parent;
		console.log(profileRef);
		return profileRef.get().then(snapshot => {
			console.log('profile snapshot data', snapshot.data())
			const profileData = snapshot.data();
			const deviceToken = profileData.deviceToken;
			console.log(profileData.deviceToken);
			return admin.messaging().send({
				data: {
					eventId: eventId
				},
				notification: {
					title: 'hola',
					body: 'hallo'
				},
				token: deviceToken
			});
		});
	});
