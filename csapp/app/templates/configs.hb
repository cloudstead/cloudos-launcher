
<div class="row margin_top">
	<div class="small-4 columns"><h3>Configurations</h3></div>
	<div class="small-4 columns">
		{{#link-to 'addlaunch' classNames="button"}}New Configuration{{/link-to}}
	</div>
</div>
<div class="row">
	<table id="providersTable" width="100%">
		<thead>
			<tr style="text-align: center">
				<th class="text_align_center">Name</th>
				<th class="text_align_center">Delete</th>
			</tr>
		</thead>
		<tbody>
			{{#each config in arrangedContent }}
				<tr id="{{config.id}}">
					<td class="text_align_center">{{config.name}}</td>
					<td class="text_align_center"><a class="button tiny" {{action "removeConfig" config.id config.name}}>Delete</a></td>
				</tr>
			{{/each}}
		</tbody>
	</table>
</div>
