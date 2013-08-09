package br.schoollabs.mykeys;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.dao.sqlite.TypeDaoSqLite;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.utils.Utils;

public class CategorySettingsActivity extends ListActivity {
	private DataDaoSqLite dataDaoSqLite;
	private CategorySettingsAdapter listAdapter;

	private Data categorySelected;
	@SuppressWarnings("unused")
	private int lastPosition = -1;
	private View selectedView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_settings);

		dataDaoSqLite = new DataDaoSqLite();
		listAdapter = new CategorySettingsAdapter(this);
		setListAdapter(listAdapter);

		registerForContextMenu(this.getListView());

		findCategories();

		clickButons();
	}

	@Override
	protected void onResume() {
		super.onResume();

		findCategories();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.category_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_category_new) {
			openDialogCategory(new Data());
			
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.category_options, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.action_category_delete:
			final Data categoryDelete = (Data) getListAdapter().getItem(info.position);

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Confirmação");
			dialog.setIcon(R.drawable.content_discard);
			dialog.setMessage("Deseja realmente excluir esta categoria?");
			dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dataDaoSqLite.remove(categoryDelete);

					findCategories();

					Utils.msg(CategorySettingsActivity.this, "Categoria excluída com sucesso!!!");
				}
			});
			dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			dialog.show();
			return true;

		case R.id.action_category_edit:
			openDialogCategory((Data) getListAdapter().getItem(info.position));

		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		categorySelected = (Data) getListAdapter().getItem(position);

		this.lastPosition = position;
		if (this.selectedView != null) {
			this.selectedView.setBackgroundColor(Color.TRANSPARENT);
		}
		this.selectedView = v;
		this.selectedView.setBackgroundColor(Color.argb(200, 0, 178, 238));
	}

	public void openDialogCategory(final Data category) {
		final AlertDialog.Builder editalert = new AlertDialog.Builder(this);

		editalert.setTitle(getString(R.string.labelCategory));

		final EditText input = new EditText(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		input.setText(category.getContent());
		editalert.setView(input);

		editalert.setPositiveButton(getString(R.string.action_save), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				category.setContent(input.getText().toString());

				saveCategory(category);
			}
		}).setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		editalert.show();
	}

	private void findCategories() {
		listAdapter.clear();
		for (Data data : dataDaoSqLite.findCategories()) {
			listAdapter.add(data);
		}
	}

	private void saveCategory(Data data) {
		String msg = "";
		
		if (data.getId() == null) {
			data.setName("Category");
			data.setOrdem(dataDaoSqLite.findMaxOrdem() + 1);
			data.setType(((TypeDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Type")).find("name", "Data"));
			
			msg = getString(R.string.msgCategorySave);
		}
		else{
			msg = getString(R.string.msgCategoryEdit);
		}

		dataDaoSqLite.save(data);
		
		Utils.msg(this, msg);

		findCategories();
	}

	private void clickButons() {
		ImageButton btnUP = (ImageButton) findViewById(R.id.buttonCategorySettingsUP);
		ImageButton btnDOWN = (ImageButton) findViewById(R.id.buttonCategorySettingsDOWN);

		btnUP.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (categorySelected != null) {
					upCategory(categorySelected);

					categorySelected = null;
				}
			}
		});

		btnDOWN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (categorySelected != null) {
					downCategory(categorySelected);

					categorySelected = null;
				}
			}
		});
	}

	private void upCategory(Data category) {
		Data categotyPrevious = dataDaoSqLite.findPreviousOrder(category.getOrdem());
		if (categotyPrevious != null) {
			category.setOrdem(categotyPrevious.getOrdem());
			categotyPrevious.setOrdem(category.getOrdem() + 1);

			dataDaoSqLite.save(categotyPrevious);
			dataDaoSqLite.save(category);
		}

		findCategories();
	}

	private void downCategory(Data category) {
		Data categotyLater = dataDaoSqLite.findLaterOrder(category.getOrdem());
		if (categotyLater != null) {
			category.setOrdem(categotyLater.getOrdem());
			categotyLater.setOrdem(category.getOrdem() - 1);

			dataDaoSqLite.save(categotyLater);
			dataDaoSqLite.save(category);
		}

		findCategories();
	}
}
