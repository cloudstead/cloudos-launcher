
<div class="row margin_top">
	<div class="small-4 columns"><h3>{{t manage_ssh.title}}</h3></div>
	<div class="small-4 columns">
		<button class="button-small" {{action "goToNewShhKey"}}>{{t manage_ssh.new_key}}</button>
	</div>
</div>
<div class="row">
	<table id="providersTable" width="100%">
		<thead>
			<tr style="text-align: center">
				<th class="text_align_center">{{t manage_ssh.key_name}}</th>
				<th class="text_align_center">{{t manage_ssh.key_actions}}</th>
			</tr>
		</thead>
		<tbody>
			{{#each sshKey in arrangedContent }}
				<tr id="{{sshKey.uuid}}">
					<td class="text_align_center">{{sshKey.name}}</td>
					<td class="text_align_center">
						<a class="button tiny" {{action "doDelete" sshKey}}>{{t manage_ssh.key_delete}}</a>
					</td>
				</tr>
			{{/each}}
		</tbody>
	</table>
</div>
<div class="row">
	{{#link-to 'add_cloudstead' classNames="button"}}{{t manage_ssh.done}}{{/link-to}}
</div>
