package co.hyperverge.android.hyperkycsample;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import co.hyperverge.hyperkyc.HyperKyc;
import co.hyperverge.hyperkyc.data.models.DocFlowConfig;
import co.hyperverge.hyperkyc.data.models.FaceFlowConfig;
import co.hyperverge.hyperkyc.data.models.HyperKycConfig;
import co.hyperverge.hyperkyc.data.models.HyperKycData;
import co.hyperverge.hyperkyc.data.models.HyperKycFlow;

public class SampleJavaActivity extends AppCompatActivity {

    private final ActivityResultLauncher<HyperKycConfig> hyperKycLauncher =
            registerForActivityResult(new HyperKyc.Contract(), result -> {
                // handle result post workflow finish/exit
                switch (result.getStatus()) {
                    case CANCELLED:
                        // user has cancelled the workflow
                        break;
                    case FAILURE:
                        // one of the flows failed
                        String reason = result.getReason();
                        break;
                    case SUCCESS:
                        // success
                        HyperKycData workflowData = result.getData();
                        break;
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStart).setOnClickListener(view -> {
            startWorkflow();
        });
    }

    private void startWorkflow() {

        DocFlowConfig docFlowConfig = new DocFlowConfig(
                true, // toggle face match for document at the end of the flow
                "ISO alpha3 country code supported by HV",
                "3 char document code supported by HV"
        );

        FaceFlowConfig faceFlowConfig = new FaceFlowConfig();

        List<HyperKycFlow> workflow = Arrays.asList(
                new HyperKycFlow.Document(docFlowConfig/*optional*/),
                new HyperKycFlow.Face(faceFlowConfig/*optional*/)
        );

        hyperKycLauncher.launch(
                // init with accessToken
                new HyperKycConfig(
                        "<jwt bearer token>",
                        workflow,
                        "<unique-id>",
                        "<ISO alpha3 country code supported by HV>"
                )
        );

        // or init with appId/appKey
        /*
        hyperKycLauncher.launch(
            HyperKycConfig(
                appId = "<appId from HV>",
                appKey = "<appKey from HV>",
                workflow = workflow,
                transactionId = "<unique-id>",
                defaultCountryId = "<ISO alpha3 country code supported by HV>"
            )
        )
        */
    }
}