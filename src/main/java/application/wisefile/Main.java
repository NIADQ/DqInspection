package application.wisefile;

import application.wisefile.common.Const;
import application.wisefile.controller.DiagnosisDataSetSelectionController;
import application.wisefile.dao.DiagnosisFileDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Objects;


public class Main extends Application {

	public static ApplicationContext context;

	public static DiagnosisFileDAO diagnosisFileDAO;

	protected  Logger logger = Logger.getLogger(this.getClass());

	private static Object monitor = new Object();
	public static Long getNextHistoryId() {
		synchronized (monitor) {
			diagnosisFileDAO.updateNextHistoryId();
			return diagnosisFileDAO.selectNextHistoryId();
		}
	}


	@Override
	public void start(Stage primaryStage) throws IOException {

		logger.info(":::::::::::::::::::::::::: 파일 진단 도구 START ::::::::::::::::::::::::::");
		try {
			Font.loadFont(getClass().getResourceAsStream("font/NanumGothic.ttf"),              14        );
			Font.loadFont(getClass().getResourceAsStream("font/NanumGothicExtraBold.ttf"),              14        );

	        Const.stage = primaryStage;
			Const.popStage = new Stage();
			Const.diagHistoryStage = new Stage();

			Const.stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/image/icon.jpg"))));
			Const.popStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/image/icon.jpg"))));
			Const.diagHistoryStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/image/icon.jpg"))));

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/DiagDataSetSelection.fxml"));
			Parent scene1_fxml = loader.load();
			Scene scene1 = new Scene(scene1_fxml);
			Const.scene1 = scene1;
			primaryStage.setScene(scene1);
			primaryStage.setTitle("표준데이터셋 진단 도구");
			primaryStage.show();

			DiagnosisDataSetSelectionController cont = loader.getController();
			cont.organNameCheck();

		} catch (IOException e) {
			logger.error("reader FileNotFoundException", e);
		}
	}


	public static void main(String[] args) {

		String[] configLocations = new String[] { "applicationContext-standalone.xml" };
		context = new ClassPathXmlApplicationContext(configLocations);
		diagnosisFileDAO = context.getBean(DiagnosisFileDAO.class);

		launch(args);
	}
}
