package com.ufukuzun.kodility.service.challenge;

import com.ufukuzun.kodility.dao.challenge.ChallengeDao;
import com.ufukuzun.kodility.domain.challenge.Challenge;
import com.ufukuzun.kodility.domain.challenge.TestCase;
import com.ufukuzun.kodility.enums.DataTypes;
import com.ufukuzun.kodility.enums.Lingo;
import com.ufukuzun.kodility.enums.ProgrammingLanguage;
import com.ufukuzun.kodility.service.language.SignatureGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeDao challengeDao;

    @Autowired
    private SignatureGeneratorService signatureGeneratorService;

    @PostConstruct  // TODO ufuk: remove after "Challenge Management" page
    public void init() {
        if (challengeDao.findAll().iterator().hasNext()) {
            return;
        }

        challengeDao.save(getSampleChallenge());
        challengeDao.save(getSecondSampleChallenge());
    }

    public List<Challenge> findAll() {
        List<Challenge> allChallenges = new ArrayList<>();
        Iterator<Challenge> iterator = challengeDao.findAll().iterator();
        while (iterator.hasNext()) {
            allChallenges.add(iterator.next());
        }

        return allChallenges;
    }

    public Challenge findById(String id) {
        return challengeDao.findById(id);
    }

    // TODO ufuk: complete - order by difficulty and find easiest challenge
    public Challenge findEasiestOne() {
        return challengeDao.findAll().iterator().next();
    }

    public Map<String, String> prepareSignaturesMapFor(Challenge challenge) {
        Map<String, String> signatures = new HashMap<>();
        for (ProgrammingLanguage language : ProgrammingLanguage.values()) {
            String signature = signatureGeneratorService.generatorSignatureFor(challenge, language);
            signatures.put(language.getShortName(), signature);
        }
        return signatures;
    }

    private Challenge getSampleChallenge() {  // TODO ufuk: remove after "Challenge Management" page
        Challenge challenge = new Challenge();

        challenge.setTitle("Sum problem");

        HashMap<Lingo, String> descriptions = new HashMap<>();
        descriptions.put(Lingo.English, "\"a\" and \"b\" are integer numbers. Write a function that sums \"a\" and \"b\", and returns.");
        descriptions.put(Lingo.Turkish, "\"a\" ve \"b\" iki tamsayıdır. \"a\" ve \"b\" tamsayılarını toplayan ve sonucu dönen bir fonksiyon yazınız.");
        challenge.setDescriptions(descriptions);

        TestCase testCase = new TestCase();
        List<Object> inputValues = new ArrayList<>();
        inputValues.add(12);
        inputValues.add(23);
        testCase.setInputs(inputValues);
        testCase.setOutput(35);

        challenge.addTestCase(testCase);

        List<String> inputTypes = new ArrayList<>();
        inputTypes.add(DataTypes.Integer.getClassName());
        inputTypes.add(DataTypes.Integer.getClassName());
        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataTypes.Integer.getClassName());

        return challenge;
    }

    private Challenge getSecondSampleChallenge() {  // TODO ufuk: remove after "Challenge Management" page
        Challenge challenge = new Challenge();

        challenge.setTitle("Subtract problem");

        HashMap<Lingo, String> descriptions = new HashMap<>();
        descriptions.put(Lingo.English, "\"a\" and \"b\" are integer numbers. Write a function that subtracts \"a\" and \"b\", and returns.");
        descriptions.put(Lingo.Turkish, "\"a\" ve \"b\" iki tamsayıdır. \"a\" ve \"b\" tamsayılarını çıkaran ve sonucu dönen bir fonksiyon yazınız.");
        challenge.setDescriptions(descriptions);

        TestCase testCase = new TestCase();
        List<Object> inputValues = new ArrayList<>();
        inputValues.add(12);
        inputValues.add(23);
        testCase.setInputs(inputValues);
        testCase.setOutput(-11);

        challenge.addTestCase(testCase);

        List<String> inputTypes = new ArrayList<>();
        inputTypes.add(DataTypes.Integer.getClassName());
        inputTypes.add(DataTypes.Integer.getClassName());
        challenge.setInputTypes(inputTypes);
        challenge.setOutputType(DataTypes.Integer.getClassName());

        return challenge;
    }

}
