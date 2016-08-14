package by.jetfire.secretsearch;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Konstantin on 14.08.2016.
 */
public class CheatDialog extends AlertDialog {

    private static final String CHEAT = "cheat";

    @Bind(R.id.cheat_text)
    protected EditText cheatText;

    private CheatListener cheatListener;

    protected CheatDialog(Context context, CheatListener cheatListener) {
        super(context);
        this.cheatListener = cheatListener;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_cheat, null);
        ButterKnife.bind(this, view);

        setView(view);
    }

    @OnClick(R.id.cheat_ok)
    protected void onOkClick() {
        String cheat = String.valueOf(cheatText.getText());
        if (CHEAT.equalsIgnoreCase(cheat.trim()) && cheatListener != null) {
            cheatListener.onCheat();
            dismiss();
        }
    }
}
