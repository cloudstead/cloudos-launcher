App.ConfigModel = Ember.Object.extend({
	toObject: function() {
		return {
			uuid: this.get("uuid"),
			name:  this.get("name"),
			account:  this.get("account"),
			base64zipData:  this.get("base64zipData"),
		};
	},

	toObjectForPost: function() {
		return {
			name:  this.get("name"),
			base64zipData:  this.get("base64zipData"),
		};
	},

	update: function() {
		var response = API.update_config(this.toObjectForPost());
		return response.isSuccess();
	},

	destroy: function() {
		var response = API.delete_config(this.get("name"));
		return response.isSuccess();
	},
});

App.ConfigModel.reopenClass({

	createNewEmpty: function() {
		return App.CloudModel.create({
			uuid: "",
			name: "",
			account: "",
			base64zipData: ""
		});
	},

	createNewFromData: function(dataObject) {
		return App.ConfigModel.create(dataObject);
	},

	createFromArray: function(configDataArray) {
		var retArray = [];
		configDataArray.forEach(function(configData) {
			retArray.push(App.ConfigModel.createNewFromData(configData));
		});

		return retArray;
	},

	getAll: function(configDataArray) {
		var response = API.get_configs();

		var dataArray = [];

		if (response.isSuccess()) {
			dataArray = response.data;
		} else {
			$.notify("Error fetching configs", { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
		}

		return App.ConfigModel.createFromArray(dataArray);
	},

	findById: function(uuid) {
		var allConfigs = App.ConfigModel.getAll();
		var configToFind = allConfigs.find(function(config) {
			return config.uuid === uuid;
		});

		return configToFind;
	}
});
