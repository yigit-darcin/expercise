package com.kodility.controller.challenge;

import com.kodility.controller.RedirectUtil;
import com.kodility.controller.challenge.model.ChallengeModel;
import com.kodility.controller.challenge.model.SaveChallengeResponse;
import com.kodility.domain.challenge.Challenge;
import com.kodility.domain.user.User;
import com.kodility.enums.DataType;
import com.kodility.enums.Lingo;
import com.kodility.enums.ManagementMode;
import com.kodility.service.challenge.ChallengeModelHelper;
import com.kodility.service.challenge.ChallengeService;
import com.kodility.service.challenge.LevelService;
import com.kodility.service.user.AuthenticationService;
import com.kodility.utils.JsonUtils;
import com.kodility.utils.validation.SaveChallengeValidator;
import com.kodility.utils.validation.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/challenges")
public class ChallengeManagementController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private SaveChallengeValidator saveChallengeValidator;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private ChallengeModelHelper challengeModelHelper;

    @RequestMapping(value = "/addChallenge", method = RequestMethod.GET)
    public ModelAndView addChallengePage() {
        return prepareChallengeManagementViewModel(ManagementMode.Add);
    }

    @RequestMapping(value = "/saveChallenge", method = RequestMethod.POST)
    @ResponseBody
    public SaveChallengeResponse saveChallenge(@RequestBody ChallengeModel challengeModel, BindingResult bindingResult) {
        saveChallengeValidator.validate(challengeModel, bindingResult);

        if (bindingResult.hasErrors()) {
            return SaveChallengeResponse.failedResponse(ValidationUtils.extractAllErrorCodes(bindingResult));
        }

        Long challengeId = challengeService.saveChallenge(challengeModel);

        return SaveChallengeResponse.successResponse(challengeId);
    }

    @RequestMapping(value = "/updateChallenge/{challengeId}", method = RequestMethod.GET)
    public ModelAndView updateChallengePage(@PathVariable("challengeId") long challengeId) {

        Challenge challenge = challengeService.findById(challengeId);

        if (isCurrentUserNotAuthorOrAdmin(challenge)) {
            return RedirectUtil.redirectHome();
        }

        ModelAndView modelAndView = prepareChallengeManagementViewModel(ManagementMode.Update);
        modelAndView.addObject("challengeModel", JsonUtils.toJsonString(challengeModelHelper.createModelFrom(challenge)));

        return modelAndView;
    }

    private ModelAndView prepareChallengeManagementViewModel(ManagementMode managementMode) {
        ModelAndView modelAndView = new ModelAndView("challenge/manageChallenge");
        modelAndView.addObject("mode", managementMode);
        modelAndView.addObject("lingos", Lingo.values());
        modelAndView.addObject("dataTypes", DataType.values());
        modelAndView.addObject("levels", levelService.getAllLevelsInOrder());
        return modelAndView;
    }

    private boolean isCurrentUserNotAuthorOrAdmin(Challenge challenge) {
        User currentUser = authenticationService.getCurrentUser();
        return !currentUser.equals(challenge.getUser()) && currentUser.isNotAdmin();
    }

}