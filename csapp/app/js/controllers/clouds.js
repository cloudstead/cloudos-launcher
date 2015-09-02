App.CloudsController = Ember.ObjectController.extend({
	originalProviders: [],
	providers: function () {
		return this.get('originalProviders');
	}.property("originalProviders"),
	providerToDelete: null,
	actions: {
		confirmProviderRemove: function (id, name) {
			console.log(id, name);
			$("#providerName").text(name);
			this.set('providerToDelete', id);
			$("#confirmProviderDelete").foundation('reveal', 'open');
		},
		removeProvider: function () {
			var providerToDelete = this.get('providerToDelete');
			console.log(providerToDelete);
			var originalProviders = this.get('originalProviders');
			originalProviders = $.grep(originalProviders, function (e) {
				return e.id != providerToDelete;
			});
			this.set('originalProviders', originalProviders);
			$("#confirmProviderDelete").foundation('reveal', 'close');
		}
	},
});