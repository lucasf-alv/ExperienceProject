package com.ProjectExperience.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /* ****************************************************************************************
     *                                   VALIDAÇÃO DE CAMPO
     * **************************************************************************************** */


    //==============================================================================
    //                      E1: CAMPOS OBRIGATÓRIOS
    //==============================================================================
    @ExceptionHandler(CorrectFieldsError.class)
    public ResponseEntity<ApiError> handleCorrectFieldsError(
            CorrectFieldsError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                java.time.LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(apiError);
    }
    //==============================================================================
    //                 E2: A IMAGEM DEVE SER UM ARQUIVO PNG OU JPG
    //==============================================================================
    @ExceptionHandler(PhotoError.class)
    public ResponseEntity<ApiError> handlePhotoError(
            PhotoError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(apiError);
    }
    /* ****************************************************************************************
     *                                   USUÁRIOS
     * **************************************************************************************** */


    //==============================================================================
    //              E3: O EMAIL OU CPF INFORMADO JÁ PERTENCE A OUTRO USUÁRIO
    //==============================================================================
    @ExceptionHandler(EmailCpfError.class)
    public ResponseEntity<ApiError> handleEmailCpfError(
            EmailCpfError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()

        );
        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //                          E4: USUÁRIO NÃO ENCONTRADO
    //==============================================================================
    @ExceptionHandler(UserNotFoundError.class)
    public ResponseEntity<ApiError> handleUsernotFound(
            UserNotFoundError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
    //=============================================================================
    //                          E5: SENHA INCORRETA
    //==============================================================================
    @ExceptionHandler(IncorrectPasswordError.class)
    public ResponseEntity<ApiError> handleIncorrectPassword(
            IncorrectPasswordError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }
    //=============================================================================
    //                         E6: ESSA CONTA FOI DESATIVADA
    //==============================================================================
    @ExceptionHandler(DesactivateAccountError.class)
    public ResponseEntity<ApiError> handleDesactivateAccountError(
            DesactivateAccountError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }
    /* ****************************************************************************************
     *                                   ATIVIDADES
     * **************************************************************************************** */



    //=============================================================================
    //                         E7: VOCÊ JÁ REGISTROU ESSA ATIVIDADE
    //==============================================================================
    @ExceptionHandler(ActivityRegisterError.class)
    public ResponseEntity<ApiError> handleActivityRegisterError(
            ActivityRegisterError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //              E8: O CRIADOR DESSA ATIVIDADE NÃO PODE SE INSCREVER COMO PARTICIPANTE
    //==============================================================================
    @ExceptionHandler(CreatorParticipantError.class)
    public ResponseEntity<ApiError> handleCreatorParticipantError(
            CreatorParticipantError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //              E9: APENAS PARTICIPANTES APROVADOS PODEM FAZER CHECK-IN
    //==============================================================================
    @ExceptionHandler(CheckInError.class)
    public ResponseEntity<ApiError> handleCheckInError(
            CheckInError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //              E10: CÓDIGO DE CONFIRMAÇÃO INCORRETO
    //==============================================================================
    @ExceptionHandler(IncorrectConfirmationCodeError.class)
    public ResponseEntity<ApiError> handleIncorrectConfirmationCodeError(
            IncorrectConfirmationCodeError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //              E11: CONFIRMAÇÃO DUPLICADA EM ATIVIDADE
    //==============================================================================
    @ExceptionHandler(ConfirmationActivityError.class)
    public ResponseEntity<ApiError> handleConfirmationActivityError(
            ConfirmationActivityError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );


        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //            E12: NÃO É POSSÍVEL SE INSCREVER EM UMA ATIVIDADE CONCLUIDA
    //==============================================================================
    @ExceptionHandler(ActivityDuplicateError.class)
    public ResponseEntity<ApiError> handleActivityDuplicateError(
            ActivityDuplicateError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );


        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //          E13: NÃO É POSSÍVEL CONFIRMAR PRESENÇA EM UMA ATIVIDADE CONCLUIDA
    //==============================================================================
    @ExceptionHandler(ConfirmationConcludeActivityError.class)
    public ResponseEntity<ApiError> handleConfirmationConcludeActivityError(
            ConfirmationConcludeActivityError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );


        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //            E14: APENAS O CRIADOR DA ATIVIDADE PODE EDITÁ-LA
    //==============================================================================
    @ExceptionHandler(EditActivityError.class)
    public ResponseEntity<ApiError> handleEditActivityError(
            EditActivityError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );


        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //            E15: APENAS O CRIADOR DA ATIVIDADE PODE EXCLUI-LÁ
    //==============================================================================
    @ExceptionHandler(DeleteActivityError.class)
    public ResponseEntity<ApiError> handleDeleteActivityError(
           DeleteActivityError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );


        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //         E16: APENAS O CRIADOR DA ATIVIDADE PODE APROVAR OU NEGAR PARTICIPANTES
    //==============================================================================
    @ExceptionHandler(ApproveParticipantsError.class)
    public ResponseEntity<ApiError> handleApproveParticipantsError(
            ApproveParticipantsError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );


        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //         E17: APENAS O CRIADOR DA ATIVIDADE PODE CONCLUI-LA
    //==============================================================================
    @ExceptionHandler(ConcludeActivityError.class)
    public ResponseEntity<ApiError> handleConcludeActivityError(
            ConcludeActivityError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );


        return ResponseEntity.badRequest().body(apiError);
    }
    //=============================================================================
    //      E18: NÃO É POSSÍVEL CANCELAR SUA INSCRIÇÃO POIS SUA PRESENÇA JÁ FOI CONFIRMADA
    //==============================================================================
    @ExceptionHandler(CancelSubscribeError.class)
    public ResponseEntity<ApiError> handleCancelSubscribeError(
            CancelSubscribeError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );


        return ResponseEntity.badRequest().body(apiError);
    }
    /* ****************************************************************************************
     *                                   AUTENTICAÇÃO
     * **************************************************************************************** */


    //=============================================================================
    //                      E19: AUTENTICAÇÃO NECESSÁRIA
    //==============================================================================
    @ExceptionHandler(AuthError.class)
    public ResponseEntity<ApiError> handleAuthError(
            AuthError ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                request.getRequestURI()
        );


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }











}
