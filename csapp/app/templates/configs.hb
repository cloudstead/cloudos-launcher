
<div class="row margin_top">
	<div class="small-4 columns"><h3>{{t configs.title}}</h3></div>
	<div class="small-4 columns">
		<button class="button-small" {{ action "doNewConfig" }}>>{{t configs.new_config}}</button>
	</div>
</div>
<div class="row">
	<table id="providersTable" width="100%">
		<thead>
			<tr style="text-align: center">
				<th class="text_align_center">>{{t configs.config_name}}</th>
				<th class="text_align_center">>{{t configs.actions}}</th>
			</tr>
		</thead>
		<tbody>
			{{#each config in arrangedContent }}
				<tr id="{{config.id}}">
					<td class="text_align_center">{{config.name}}</td>
					<td class="text_align_center">
						<a class="button tiny" {{action "removeConfig" config.id config.name}}>
							{{t configs.delete}}
						</a>
					</td>
				</tr>
			{{/each}}
		</tbody>
	</table>
</div>
