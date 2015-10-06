Notify = {
	globally: function(message) {
		$.notify(message, { position:"bottom",autoHideDelay: 1800 });
	},

	onElement: function(element, message) {
		element.notify( message, { position:"bottom",autoHideDelay: 1800 } );
	},
};
