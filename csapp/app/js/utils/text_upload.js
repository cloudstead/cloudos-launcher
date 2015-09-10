App.TextFileUpload = Ember.TextField.extend({
	type: 'file',
	attributeBindings: ['name'],
	change: function (evt) {
		var input = evt.target;
		var self = this;

		// We're using a single upload, but multiple could be
		// supported by adding `multiple` on the input element
		// and iterating over the files list here.
		if (input.files && input.files[0]){
			reader = new FileReader();
			reader.onload = function(e) {
				uploadedFile = e.target.result;

				// Perform the action configured for this instance
				self.sendAction('action', uploadedFile);
			};
			reader.readAsText(input.files[0]);
		}
	}
});
