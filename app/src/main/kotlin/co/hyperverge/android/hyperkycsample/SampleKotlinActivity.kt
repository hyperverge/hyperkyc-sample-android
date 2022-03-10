package co.hyperverge.android.hyperkycsample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import co.hyperverge.hyperkyc.HyperKyc
import co.hyperverge.hyperkyc.data.models.*

class SampleKotlinActivity : AppCompatActivity() {

    private val hyperKycLauncher = registerForActivityResult(HyperKyc.Contract()) { result ->
        // handle result post workflow finish/exit
        when (result.status) {
            HyperKycResult.Status.CANCELLED -> {
                // user has cancelled the workflow
            }
            HyperKycResult.Status.FAILURE -> {
                // one of the flows failed
                val reason = result.reason
            }
            HyperKycResult.Status.SUCCESS -> {
                val workflowData = result.data
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            startWorkflow()
        }
    }

    private fun startWorkflow() {

        val docFlowConfig = DocFlowConfig(
            countryId = "ISO alpha3 country code supported by HV",
            documentId = "3 char document code supported by HV",
            useForFaceMatch = true // toggle face match for document at the end of the flow
        )

        val faceFlowConfig = FaceFlowConfig()

        val workflow = listOf(
            HyperKycFlow.Document(docFlowConfig/*optional*/),
            HyperKycFlow.Face(faceFlowConfig/*optional*/)
        )

        hyperKycLauncher.launch(
            // init with accessToken
            HyperKycConfig(
                accessToken = "<jwt bearer token>",
                workflow = workflow,
                transactionId = "<unique-id>",
                defaultCountryId = "<ISO alpha3 country code supported by HV>"
            )
        )

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