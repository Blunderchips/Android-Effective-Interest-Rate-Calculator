package dot.empire.calc.eirc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vj.widgets.AutoResizeTextView;

public final class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText txtR;
    private EditText txtM;
    private EditText txtT;
    private AutoResizeTextView txtOutput;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        ((AutoResizeTextView) findViewById(R.id.formula)).setText(Html.fromHtml(
                "I<sub>t</sub> = (1 + <sup>r</sup>&frasl;<sub>m</sub>)<sup>mt</sup> - 1"));

        ((Button) findViewById(R.id.btnCalculate)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnClear)).setOnClickListener(this);

        this.txtR = (EditText) findViewById(R.id.txtR);
        this.txtM = (EditText) findViewById(R.id.txtM);
        this.txtT = (EditText) findViewById(R.id.txtT);

        this.txtOutput = (AutoResizeTextView) findViewById(R.id.txtOutput);

        try {
            FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
            analytics.setAnalyticsCollectionEnabled(true);

            MobileAds.initialize(this, getResources().getString(R.string.adUnitId));

            this.adView = (AdView) findViewById(R.id.adView);

            AdRequest.Builder builder = new AdRequest.Builder();
            this.adView.loadAd(builder.build());
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCalculate:
                this.calculate();
                return;
            case R.id.btnClear:
                this.txtR.setText("");
                this.txtM.setText("");
                this.txtT.setText("");
                return;
        }
    }

    private void calculate() {
        try {
            if (txtR.getText().toString().isEmpty()
                    || txtM.getText().toString().isEmpty()
                    || txtT.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter valid inputs.", Toast.LENGTH_SHORT).show();
                return;
            }

            float r = Float.parseFloat(txtR.getText().toString()) / 100;
            float m = Float.parseFloat(txtM.getText().toString());
            float t = Float.parseFloat(txtT.getText().toString());

            double ans = Math.pow((1 + (r / m)), m * t) - 1;
            this.txtOutput.setText(String.format("%.2f", (float) (ans * 100)) + "%");
        } catch (Exception ex) {
            Toast.makeText(this, ex.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
            this.txtOutput.setText(ex.getMessage());
        }
    }

    /**
     * Called when leaving the activity.
     */
    @Override
    public void onPause() {
        if (adView != null) {
            this.adView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            this.adView.resume();
        }
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        if (adView != null) {
            this.adView.destroy();
        }
        super.onDestroy();
    }
}
