package be.pxl.clubs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST)
public class ClubException extends RuntimeException{
    public ClubException(String message) {super(message);}
}
