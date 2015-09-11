App.CloudsController = App.BaseArrayController.extend({
	providerToDelete: null,
	actions: {
		doAddCloud: function() {
			this.send("doTransitionTo", "add_cloud");
		},

		doDelete: function(cloud) {
			if (cloud.destroy()) {
				this.get('content').removeObject(cloud);
				console.log("CONTENT: ", this.get("content"));
			}
		},

		confirmProviderRemove: function (id, name) {
			// console.log(id, name);
			// $("#providerName").text(name);
			// this.set('providerToDelete', id);
			// $("#confirmProviderDelete").foundation('reveal', 'open');
		},
		removeProvider: function () {
			// var providerToDelete = this.get('providerToDelete');
			// console.log(providerToDelete);
			// var originalProviders = this.get('originalProviders');
			// originalProviders = $.grep(originalProviders, function (e) {
			// 	return e.id !== providerToDelete;
			// });
			// this.set('originalProviders', originalProviders);
			// $("#confirmProviderDelete").foundation('reveal', 'close');
		}
	},
});
