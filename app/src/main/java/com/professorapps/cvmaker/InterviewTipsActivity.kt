package com.professorapps.cvmaker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.professorapps.cvmaker.adapters.InterviewTipsAdapter
import com.professorapps.cvmaker.models.InterviewTipsModel
import java.util.Collections

class InterviewTipsActivity : AppCompatActivity() {
    var about: String? = null
    var adapter: InterviewTipsAdapter? = null
    var back: LinearLayout? = null
    var imgnexttips: ImageView? = null
    var itemdata: InterviewTipsModel? = null
    var layout_recycleview: RelativeLayout? = null
    var rcList: RecyclerView? = null
    var dataList: MutableList<InterviewTipsModel?>? = null
    private val backgroundColors = intArrayOf(
        R.color.list_color1,
        R.color.list_color2,
        R.color.list_color3,
        R.color.list_color4,
        R.color.list_color5,
        R.color.list_color6,
        R.color.list_color7
    )


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_interview_tips)
        initToolbar()
        Toast.makeText(this, "Tap for next tips", Toast.LENGTH_SHORT).show()
        this.imgnexttips = findViewById<View>(R.id.imgnexttips) as ImageView
        this.layout_recycleview = findViewById<View>(R.id.layout_recycleview) as RelativeLayout
        this.rcList = findViewById<View>(R.id.rcList) as RecyclerView
        rcList!!.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(this.rcList)
        initData()
        Collections.shuffle(this.dataList)
        this.adapter = InterviewTipsAdapter(
            this,
            dataList!!
        )
        rcList!!.adapter = this.adapter
        adapter!!.notifyDataSetChanged()
        imgnexttips!!.setOnClickListener {
            InterviewTipsAdapter.selectedposition++
            Collections.shuffle(this@InterviewTipsActivity.dataList)
            adapter!!.notifyDataSetChanged()
            Log.d("rclistclick>>", InterviewTipsAdapter.selectedposition.toString() + "")
        }
    }
    private fun initData() {
        if (dataList!!.isEmpty()) {
            dataList?.apply {
                add(
                    InterviewTipsModel(
                        "Start by researching the company and your interviewers",
                        "Understanding key information about the company you’re interviewing with can help you go into your interview with confidence. Using the company’s website, social media posts and recent press releases will provide a solid understanding of what the company’s goals and how your background makes you a great fit."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Practice your answers to common interview questions",
                        "Prepare your answer to the common question: “Tell me about yourself, and why are you interested in this role with our company?”. The idea is to quickly communicate who you are and what value you will bring to the company and the role—it’s your personal elevator pitch."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Re-read the job description",
                        "You may want to print it out and begin underlining specific skills the employer is looking for. Think about examples from your past and current work that align with these requirements."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Recruit a friend to practice answering questions",
                        "Actually practicing your answers out loud is an incredibly effective way to prepare. Say them to yourself or ask a friend to help run through questions and answers. You’ll find you gain confidence as you get used to saying the words."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Prepare a list of references",
                        "Your interviewers might require you to submit a list of references before or after your interview. Having a reference list prepared ahead of time can help you quickly complete this step to move forward in the hiring process."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Be prepared with examples of your work",
                        "During the interview, you will likely be asked about specific work you’ve completed in relation to the position. After reviewing the job description, think of work you’ve done in past jobs, clubs or volunteer positions that show you have experience and success doing the work they require."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Plan your interview attire the night before",
                        " If you’re speaking to a recruiter before the interview, you can ask them about the dress code in the workplace and choose your outfit accordingly. If you don’t have someone to ask, research the company to learn what’s appropriate."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Bring copies of your resume, a notebook and pen",
                        "Take at least five copies of your printed resume on clean paper in case of multiple interviewers. Highlight specific accomplishments on your copy that you can easily refer to and discuss. Bring a pen and small notebook. Prepare to take notes, but not on your smartphone or other electronic device. Write information down so that you can refer to these details in your follow-up thank you notes. Maintain eye contact as much as possible."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Plan your schedule so that you can arrive 10–15 minutes early",
                        "Map out your route to the interview location so you can be sure to arrive on time. Consider doing a practice run. If you’re taking public transportation, identify a backup plan if there are delays or closures."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Make a great first impression",
                        "Don’t forget the little things—shine your shoes, make sure your nails are clean and tidy, and check your clothes for holes, stains, pet hair and loose threads. Display confident body language and a smile throughout."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Treat everyone you encounter with respect",
                        "This includes people on the road and in the parking lot, security personnel and front desk staff. Treat everyone you don’t know as though they’re the hiring manager. Even if they aren’t, your potential employer might ask for their feedback."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Practice good manners and body language",
                        "Practice confident, accessible body language from the moment you enter the building. Sit or stand tall with your shoulders back. Before the interview, take a deep breath and exhale slowly to manage feelings of anxiety and encourage self-confidence. The interviewer should extend their hand first to initiate a handshake. Stand, look the person in the eye and smile. A good handshake should be firm but not crush the other person’s fingers."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Win them over with your authenticity and positivity",
                        "Being genuine during interview conversations can help employers easily relate to you. Showing positivity with a smile and upbeat body language can help keep the interview light and constructive."
                    )
                )
               add(
                    InterviewTipsModel(
                        "Respond truthfully to the questions asked",
                        "While it can seem tempting to embellish on your skills and accomplishments, interviewers find honesty refreshing and respectable. Focus on your key strengths and why your background makes you uniquely qualified for the position."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Tie your answers back to your skills and accomplishments",
                        "With any question you answer, it is important that you tie your background to the job by providing examples of solutions and results you’ve achieved. Use every opportunity to address requirements listed in the job description."
                    )
                )
               add(
                    InterviewTipsModel(
                        "Keep your answers concise and focused",
                        "Your time with each interviewer is limited so be mindful of rambling. Practicing your answers beforehand can help keep you focused."
                    )
                )
               add(
                    InterviewTipsModel(
                        "Do not speak negatively about your previous employers",
                        "Companies want to hire problem solvers who overcome tough situations. If you’re feeling discouraged about your current job, focus on talking about what you’ve gained from that experience and what you want to do next."
                    )
                )
                add(
                    InterviewTipsModel(
                        "Ask about next steps",
                        "After your interview, it is appropriate to ask either your interviewer, hiring manager or recruiter about what you should expect next. This will likely be a follow-up email with results from your interview, additional requirements like an assignment or reference list or another interview."
                    )
                )

                        add(
                    InterviewTipsModel(
                        "Send a personalized thank you letter after the interview",
                        "Send personalized thank you notes to each interviewer. Ask for the business card of each person you speak with during the interview process so that you can follow up individually with a separate thank you email. If you interviewed in the morning, send your follow-up emails the same day. If you interviewed in the afternoon, the next morning is fine. Make certain that each each email is distinct from the others, using the notes you took during the conversations."
                    )
                )
            }
        }
    }


    private fun initToolbar() {
        this.back = findViewById<View>(R.id.back) as LinearLayout
        back!!.setOnClickListener { this@InterviewTipsActivity.onBackPressed() }
    }

    companion object {
        const val EXTRA_STICKER_ID: String = "extra_sticker_id"
    }
}
