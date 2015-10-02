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
		return API.update_config(this.toObjectForPost());
	},

	destroy: function() {
		return API.delete_config(this.get("name"));
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

	getAll: function() {
		return API.get_configs().then(function(configs){
			return App.ConfigModel.createFromArray(configs);
		});
	},
});
