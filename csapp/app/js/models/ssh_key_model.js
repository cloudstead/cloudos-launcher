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
		var response = API.create_ssh_key(this.toObjectForPost());
		return response.isSuccess();
	},

	destroy: function() {
		var response = API.delete_ssh_key(this.get("name"));
		return response.isSuccess();
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
		var response = API.get_ssh_keys();

		var dataArray = [];

		if (response.isSuccess()) {
			dataArray = response.data;
		} else {
			$.notify("Error fetching ssh keys", { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
		}

		return App.SshKeyModel.createFromArray(dataArray);
	}
});
