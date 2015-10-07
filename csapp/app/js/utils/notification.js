// Notification API Object. Internally uses vendor/notify.js
Notify = {
	autoHideInMs: 1800,

	globally: function(message) {
		$.notify(message, { position:"bottom", autoHideDelay: this.autoHideInMs });
	},

	onElement: function(element, message) {
		element.notify( message, { position:"bottom", autoHideDelay: this.autoHideInMs } );
	},
};
