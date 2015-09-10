<div class="row">
	<div class="small-3 columns launcher_header_item">
		{{#link-to 'clouds'}}
			<div class="box">
				<img src="/img/cloud.png" class="box_img">
				<p class="box_name">clouds</p>
			</div>
		{{/link-to}}
	</div>
	<div class="small-3 columns launcher_header_item">
		{{#link-to 'configs'}}
		<div class="box">
			<img src="/img/settings.png" class="box_img">
			<p class="box_name">configs</p>
		</div>
		{{/link-to}}
	</div>
	<div class="small-3 columns launcher_header_item">
		{{#link-to 'cloudsteads'}}
		<div class="box">
			<img src="/img/exit.png" class="box_img">
			<p class="box_name">cloudstead</p>
		</div>
		{{/link-to}}
	</div>
	<div class="small-3 columns launcher_header_item">
		<a {{action "doLogout"}}>
		<div class="box">
			<img src="/img/exit.png" class="box_img">
			<p class="box_name">Logout</p>
		</div>
		</a>
	</div>
</div>

{{outlet}}
