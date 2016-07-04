package com.maudit.utils;

/**
 * Created by Prakash on 2/25/2016.
 */
public class Offline {
    public static String sectionResponse = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "   <soap:Body>\n" +
            "      <GetChecklistResponse xmlns=\"http://tempuri.org/\">\n" +
            "         <GetChecklistResult>\n" +
            "            <ScheduledChecklist xmlns=\"\">\n" +
            "               <ChecklistResult>\n" +
            "                  <Result>True</Result>\n" +
            "                  <MessageText>Checklist Sections are available</MessageText>\n" +
            "                  <AuditScheduleID>1033</AuditScheduleID>\n" +
            "                  <ScheduleChecklistID>1034</ScheduleChecklistID>\n" +
            "                  <ScheduleStatus>3</ScheduleStatus>\n" +
            "                  <Auditors>\n" +
            "                     <AuditorStatus>3</AuditorStatus>\n" +
            "                     <IsLeadAuditor>1</IsLeadAuditor>\n" +
            "                     <Location>\n" +
            "                        <LocationName>Location 1</LocationName>\n" +
            "                        <StartDate>2015-11-24</StartDate>\n" +
            "                        <Users>\n" +
            "                           <CreatedByUser>admin</CreatedByUser>\n" +
            "                           <ChecklistName>Nursing Nursing Auditcalc</ChecklistName>\n" +
            "                           <ModifiedDate>24 Nov 2015  8:33AM</ModifiedDate>\n" +
            "                           <AssignedAuditors/>\n" +
            "                           <AssignedSections>ACC,PCI,QPS</AssignedSections>\n" +
            "                           <AuditorStatusText>\n" +
            "                              <AuditorStatusName>In Progress</AuditorStatusName>\n" +
            "                           </AuditorStatusText>\n" +
            "                        </Users>\n" +
            "                     </Location>\n" +
            "                  </Auditors>\n" +
            "               </ChecklistResult>\n" +
            "               <Checklist>\n" +
            "                  <Sections>\n" +
            "                     <SectionID>5630</SectionID>\n" +
            "                     <SectionAbbr>ACC</SectionAbbr>\n" +
            "                     <SectionTitle>Care of Intravenous System</SectionTitle>\n" +
            "                     <ResultStatus>0</ResultStatus>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20741</ScheduleQuestionID>\n" +
            "                        <QAbbrv>ACC.1</QAbbrv>\n" +
            "                        <QuestionText>What is care of intravenous system?</QuestionText>\n" +
            "                        <Hint>test</Hint>\n" +
            "                        <StandardORClauses>ACC.1</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>1</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <ScheduleAnswerID>12444</ScheduleAnswerID>\n" +
            "                           <ScheduleAnswerTypeID>3153</ScheduleAnswerTypeID>\n" +
            "                           <ScheduleAnswerSectionID>1392</ScheduleAnswerSectionID>\n" +
            "                           <QAnswerValue>2</QAnswerValue>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20744</ScheduleQuestionID>\n" +
            "                        <QAbbrv>ACC.2</QAbbrv>\n" +
            "                        <QuestionText>Staff is able to verbalize the immediate interventions and follow up care on phlebitis</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>ACC.2</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>2</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <ScheduleAnswerID>12445</ScheduleAnswerID>\n" +
            "                           <ScheduleAnswerTypeID>3152</ScheduleAnswerTypeID>\n" +
            "                           <ScheduleAnswerSectionID>1392</ScheduleAnswerSectionID>\n" +
            "                           <QAnswerValue>1</QAnswerValue>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20745</ScheduleQuestionID>\n" +
            "                        <QAbbrv>ACC.3</QAbbrv>\n" +
            "                        <QuestionText>Use of Restraint is appropriate and reason is indicated</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>ACC.3</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>3</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20746</ScheduleQuestionID>\n" +
            "                        <QAbbrv>ACC.4</QAbbrv>\n" +
            "                        <QuestionText>Patients in restraints is monitored at least 2 hourly</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>ACC,4</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>4</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                  </Sections>\n" +
            "                  <Sections>\n" +
            "                     <SectionID>5631</SectionID>\n" +
            "                     <SectionAbbr>PCI</SectionAbbr>\n" +
            "                     <SectionTitle>Prevention and Control of Infections</SectionTitle>\n" +
            "                     <ResultStatus>0</ResultStatus>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20742</ScheduleQuestionID>\n" +
            "                        <QAbbrv>PCI.2</QAbbrv>\n" +
            "                        <QuestionText>What is prevention and control of infection?</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>PCI.2</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>1</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20747</ScheduleQuestionID>\n" +
            "                        <QAbbrv>PCI.2</QAbbrv>\n" +
            "                        <QuestionText>NGT marking is present and secured</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>PCI.2</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>2</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20748</ScheduleQuestionID>\n" +
            "                        <QAbbrv>PCI.3</QAbbrv>\n" +
            "                        <QuestionText>Patient is fed as per scenario in the NGT algorithm where appropriate</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>PCI.3</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>3</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20749</ScheduleQuestionID>\n" +
            "                        <QAbbrv>PCI.4</QAbbrv>\n" +
            "                        <QuestionText>Infection Control measures are adhered to in the feeding process.</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>PCI.3</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>4</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                  </Sections>\n" +
            "                  <Sections>\n" +
            "                     <SectionID>5632</SectionID>\n" +
            "                     <SectionAbbr>QPS</SectionAbbr>\n" +
            "                     <SectionTitle>Identify Patients Correctly (IPSG 1)</SectionTitle>\n" +
            "                     <ResultStatus>0</ResultStatus>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20743</ScheduleQuestionID>\n" +
            "                        <QAbbrv>QPS.1</QAbbrv>\n" +
            "                        <QuestionText>What is IPSG 1?</QuestionText>\n" +
            "                        <Hint>test</Hint>\n" +
            "                        <StandardORClauses>IPSG.1</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>1</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20750</ScheduleQuestionID>\n" +
            "                        <QAbbrv>QPS.2</QAbbrv>\n" +
            "                        <QuestionText>Staff knows when to use two patient identifiers.</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>IPSG.1</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>2</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20751</ScheduleQuestionID>\n" +
            "                        <QAbbrv>QPS.3</QAbbrv>\n" +
            "                        <QuestionText>Staff practice 'read back' when receiving critical result (Observation/discussion)</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>IPSG.2</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>3</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                     <Questions>\n" +
            "                        <ScheduleQuestionID>20752</ScheduleQuestionID>\n" +
            "                        <QAbbrv>QPS.4</QAbbrv>\n" +
            "                        <QuestionText>Update of patient's condition to patient/NOK is done daily and documented,  unless, the duration for update is specify by patient/NOK (Check Kardex).</QuestionText>\n" +
            "                        <Hint/>\n" +
            "                        <StandardORClauses>IPSG.2</StandardORClauses>\n" +
            "                        <IsMandatory>0</IsMandatory>\n" +
            "                        <Sequence>4</Sequence>\n" +
            "                        <Answers>\n" +
            "                           <IsSectionAssigned>1</IsSectionAssigned>\n" +
            "                        </Answers>\n" +
            "                     </Questions>\n" +
            "                  </Sections>\n" +
            "               </Checklist>\n" +
            "               <ChecklistConfigurations>\n" +
            "                  <Configuration>\n" +
            "                     <IsChecklist>1</IsChecklist>\n" +
            "                     <IsAnswerLevel>1</IsAnswerLevel>\n" +
            "                     <IsRemark>1</IsRemark>\n" +
            "                     <IsMandatory>1</IsMandatory>\n" +
            "                     <IsSubSection>0</IsSubSection>\n" +
            "                     <SectionColumnType>3</SectionColumnType>\n" +
            "                     <RepeatSectionTitle>Case date</RepeatSectionTitle>\n" +
            "                     <ShowStatus>0</ShowStatus>\n" +
            "                     <ShowScore>1</ShowScore>\n" +
            "                  </Configuration>\n" +
            "               </ChecklistConfigurations>\n" +
            "               <AnswerGroups>\n" +
            "                  <AnswerSections>\n" +
            "                     <ScheduleAnswerSectionID>1392</ScheduleAnswerSectionID>\n" +
            "                     <ScheduleAnsSection/>\n" +
            "                     <ScheduleChecklistID>1034</ScheduleChecklistID>\n" +
            "                  </AnswerSections>\n" +
            "                  <AnswerSections>\n" +
            "                     <ScheduleAnswerSectionID>1393</ScheduleAnswerSectionID>\n" +
            "                     <ScheduleAnsSection/>\n" +
            "                     <ScheduleChecklistID>1034</ScheduleChecklistID>\n" +
            "                  </AnswerSections>\n" +
            "               </AnswerGroups>\n" +
            "               <AnswerColumns>\n" +
            "                  <AnswerType>\n" +
            "                     <ScheduleAnswerTypeID>3152</ScheduleAnswerTypeID>\n" +
            "                     <ScheduleAnsOptions>Yes</ScheduleAnsOptions>\n" +
            "                     <ScheduleAnsOptionValue>1</ScheduleAnsOptionValue>\n" +
            "                     <AnswerColor>006600</AnswerColor>\n" +
            "                     <ScheduleChecklistID>1034</ScheduleChecklistID>\n" +
            "                  </AnswerType>\n" +
            "                  <AnswerType>\n" +
            "                     <ScheduleAnswerTypeID>3153</ScheduleAnswerTypeID>\n" +
            "                     <ScheduleAnsOptions>No</ScheduleAnsOptions>\n" +
            "                     <ScheduleAnsOptionValue>2</ScheduleAnsOptionValue>\n" +
            "                     <AnswerColor>CC0000</AnswerColor>\n" +
            "                     <ScheduleChecklistID>1034</ScheduleChecklistID>\n" +
            "                  </AnswerType>\n" +
            "                  <AnswerType>\n" +
            "                     <ScheduleAnswerTypeID>3154</ScheduleAnswerTypeID>\n" +
            "                     <ScheduleAnsOptions>NA</ScheduleAnsOptions>\n" +
            "                     <ScheduleAnsOptionValue>3</ScheduleAnsOptionValue>\n" +
            "                     <AnswerColor>FFCC66</AnswerColor>\n" +
            "                     <ScheduleChecklistID>1034</ScheduleChecklistID>\n" +
            "                  </AnswerType>\n" +
            "               </AnswerColumns>\n" +
            "               <MultiMedia/>\n" +
            "            </ScheduledChecklist>\n" +
            "         </GetChecklistResult>\n" +
            "      </GetChecklistResponse>\n" +
            "   </soap:Body>\n" +
            "</soap:Envelope>";
}
