<div class="row">
	<div id="sidebar" class="medium-3 columns">
		<ul class="vertical menu-bar wiz_panel">
			<h3 class="wiz_panel_header">Settings</h3>
			{{#each linkItem in model.linkItems}}
				<li><a {{action "updateSelection" linkItem.name}} class="menu-item {{unbound linkItem.name}}">{{linkItem.value}}</a></li>
			{{/each}}
		</ul>
	</div>
	<div id="main" class="medium-9 columns">
		{{outlet}}
	</div>
</div>