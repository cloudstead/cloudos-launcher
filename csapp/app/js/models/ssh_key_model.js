App.SshKeyModel = Ember.Object.extend({
	toObject: function() {
		return {
			uuid: this.get("uuid"),
			name: this.get("name"),
			publicKey: this.get("publicKey"),
			account: this.get("account")
		};
	},

	toObjectForPost: function() {
		return {
			name:  this.get("name"),
			publicKey:  this.get("publicKey"),
		};
	},

	update: function() {
		return API.create_ssh_key(this.toObjectForPost());
	},

	destroy: function() {
		return API.delete_ssh_key(this.get("name"));
	},
});

App.SshKeyModel.reopenClass({
	createNewEmpty: function() {
		return App.SshKeyModel.create({
			uuid: "",
			name: "",
			publicKey: "",
			account: "",
		});
	},

	createNewFromData: function(dataObject) {
		return App.SshKeyModel.create(dataObject);
	},

	createFromArray: function(cloudsteadDataArray) {
		var retArray = [];
		cloudsteadDataArray.forEach(function(cloudsteadData) {
			retArray.push(App.SshKeyModel.createNewFromData(cloudsteadData));
		});

		return retArray;
	},

	getAll: function() {
		return API.get_ssh_keys().then(function(ssh_keys){
			return App.SshKeyModel.createFromArray(ssh_keys);
		});
	},
});
