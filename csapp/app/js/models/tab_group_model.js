App.TabGroupModel = Ember.Object.extend({
	addTab: function(tab) {
		var tabs = this.get("tabs");
		tabs.push(tab);
		this.set("tabs", tabs);
	}
});

App.TabGroupModel.reopenClass({
	createNew: function(groupName, translation) {
		return App.TabGroupModel.create({
			tabGroupName: groupName,
			tabGroupCaption: Ember.String.htmlSafe(translation['label']),
			tabGroupHelp: Ember.String.htmlSafe(translation['info']),
			tabs: []
		});
	}
});
