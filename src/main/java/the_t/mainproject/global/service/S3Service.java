package the_t.mainproject.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import the_t.mainproject.global.exception.BusinessException;
import the_t.mainproject.global.exception.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3Service {
	@Value("${cloud.aws.s3.bucket}")
	private String BUCKET;

	private final AmazonS3 amazonS3;

	@Transactional
	public String uploadImage(MultipartFile image) {
		if(image == null){
			throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
		}
		String saveFileName =  image.getOriginalFilename().substring(0, image.getOriginalFilename().lastIndexOf("."))
				+ "-" + convertToRandomName(image.getOriginalFilename());

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(image.getSize());
		metadata.setContentType(image.getContentType());

		try (InputStream inputStream = image.getInputStream()) {
			// S3에 업로드 및 저장
			amazonS3.putObject(new PutObjectRequest(BUCKET, saveFileName, inputStream, metadata));
		} catch (IOException e) {
			throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
		}
		return getUrl(saveFileName);
	}

	public String convertToRandomName(String originalFileName) {
		String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		return UUID.randomUUID().toString().concat(fileExtension);
	}

	public String getUrl(String s3key) { //s3key == uploadImage 메소드의 savedFileName과 동일
		if(s3key == null){
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다.");
		}
		return amazonS3.getUrl(BUCKET, s3key).toString();
	}
}
