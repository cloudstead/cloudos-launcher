App.CloudsRoute = Ember.Route.extend({
	setupController: function(controller, model) {
		for(i = 0; i< 5; i++){
			controller.get('originalProviders').push({
				id: i,
				name: "server" + i,
				provider: "provider" + i,
			});
		}
	},
});