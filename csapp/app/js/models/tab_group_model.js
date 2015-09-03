App.TabGroupModel = Ember.Object.extend({
	addTab: function(tab) {
		var tabs = this.get("tabs");
		tabs.push(tab);
		this.set("tabs", tabs);
	}
});

App.TabGroupModel.reopenClass({
	createNew: function(groupName, data) {

		var dataPath = data.split("/");
		var appName = dataPath[0];
		var category = dataPath[1];
		var field = dataPath[2];

		var translationURL = APPS_DATA_PATH + appName + "/" + TRANSLATION_FILENAME;

		$.ajax({
			dataType: "json",
			url: translationURL,
			async: false,
			success: function (data) {
				translation = data["categories"][category][field];
			}
		});

		return App.TabGroupModel.create({
			tabGroupName: groupName,
			tabGroupCaption: Ember.String.htmlSafe(translation['label']),
			tabGroupHelp: Ember.String.htmlSafe(translation['info']),
			tabs: []
		});
	// createNew: function(groupName, translation) {
	// 	return App.TabGroupModel.create({
	// 		tabGroupName: groupName,
	// 		tabGroupCaption: Ember.String.htmlSafe(translation['label']),
	// 		tabGroupHelp: Ember.String.htmlSafe(translation['info']),
	// 		tabs: []
	// 	});
	}
});
