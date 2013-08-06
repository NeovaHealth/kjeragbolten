package uk.kch.nhs.geneva.kjeragbolten.route

import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder

class MtaRouteBuilder extends RouteBuilder {

  override def configure() = {

    val props = System.getProperties()
    props.setProperty("mail.mime.multipart.ignoremissingboundaryparameter", "true")
    props.setProperty("mail.mime.multipart.allowempty", "true")
    
    from("james-smtp:localhost:26")
      .process(new MailExchangeProcessor)
      .setHeader("to", constant("elijah.charles@nhs.net"))
      .setHeader("From", constant("epr.notifications@nhs.net"))
      .to("smtp:10.148.129.237:25")
      .process(new MailToCdaProcessor)
      .process(new CcdDocumentDataProcessor)
      .to("cxf://https://edt-hub.kch.nhs.uk/itk2/itk2adapter.asmx?serviceClass=uk.kch.nhs.geneva.kjeragbolten.generated.services.itk.SendCDADocumentV20Ptt");

  }

}