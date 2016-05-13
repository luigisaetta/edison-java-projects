package edison;

import upm_grove.*;
import upm_gas.*;
import org.eclipse.paho.client.mqttv3.*;

public class EdisonDevice
{
	public static void main(String[] args)
	{
		// handle configuration
		Config config = new Config();
		config.readConfig();
		
		System.out.println("Starting program...");

		GroveTemp tempSensor = new GroveTemp(config.PIN_TEMP);
		GroveLight lightSensor = new GroveLight(config.PIN_LIGHT);
        TP401 aqsSensor = new TP401(config.PIN_AQS);
        
		MqttClient mqttClient = null;

		try
		{
			mqttClient = new MqttClient(config.BROKER, config.CLIENTID);

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			System.out.println("Connecting to broker... ");

			mqttClient.connect(connOpts);

			System.out.println("Connected!");

		} catch (MqttException e1)
		{
			e1.printStackTrace();

			System.out.println("Exiting!");
			System.exit(-1);
		}

		int i = 0;

		while (true)
		{
			// read value from sensors
			float temp = tempSensor.value();
			float light = lightSensor.raw_value();
            int airQuality = aqsSensor.getSample();

			System.out.println("Iteration n. " + i++);

			String sMsg = "Temperature is : " + temp + ", Light is: " + light + ", Air Quality is: " + airQuality ;
			System.out.println(sMsg);

			DeviceMessage msg = new DeviceMessage(config.CLIENTID, temp, light, airQuality);

			try
			{
				// send the message in JSON format {temp: 33, light: 101, airQuality: 55}
				MqttMessage message = new MqttMessage(msg.toJSONString()
						.getBytes());
				message.setQos(config.QOS);

				// publish to the topic
				if (mqttClient.isConnected())
				{
					mqttClient.publish(config.TOPIC, message);
				}

			} catch (MqttException e1)
			{
				e1.printStackTrace();
			}

			// wait till the next cycle
			try
			{
				Thread.sleep(config.SLEEP_TIME);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}