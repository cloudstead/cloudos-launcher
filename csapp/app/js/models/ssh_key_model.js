App.SshKeyModel = Ember.Object.extend({
	toJSObject: function() {
		return {
			uuid: this.get("uuid"),
			name: this.get("name"),
			publicKey: this.get("publicKey"),
			account: this.get("account")
		};
	}
});

App.SshKeyModel.reopenClass({
	createNewEmpty: function() {
		return App.SshKeyModel.create({
			uuid: "",
			name: "",
			publicKey: "",
			account: "",
		});
	}
});
