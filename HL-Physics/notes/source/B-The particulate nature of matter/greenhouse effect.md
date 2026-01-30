Radiation that arrives at the Earth's surface is **black-body radiation** from the Sun, which is considered a **black-body**.

Thermal radiation is emitted as electromagnetic waves by **all objects** at temperatures greater than 0 Kelvin. EM waves travel through a vacuum.

A **black body** absorbs all the radiation incident on it. A black body also emits radiation with a pattern characterised by its temperature; it is **black-body** radiation.

![[Pasted image 20250818210326.png]] ^2b7f05

**Intensity** $I$ of radiation is the power $P$ emitted per unit area;
$$
I=\frac{P}{A}
$$
where $A$ is the surface area of the emitting object.

The area under [[#^2b7f05|this curve]] is given by the **Stefan-Boltzmann law**, which predicts that the **intensity output by a black-body radiator** is $I=\sigma T^4$ where $\sigma$ is the Stefan-Boltzmann constant.

In astrophysics, $P$ is known as the **luminosity** $L$ of a perfect **black body**:
$$
\begin{aligned}
	I = \frac{P}{A} &\equiv \frac{L}{A}\\
	\sigma T^4 &= \frac{L}{A} \qquad \because \text{Stefan-Boltzmann law}\\
	\therefore L &= A\sigma T^4
\end{aligned}
$$
For a grey body, $L=eA\sigma T^4$ where $e$ is the emissivity of the body.

---
**Wien's displacement law** predicts the characteristic peak in [[#^2b7f05|this graph]] is given as:
$$
\begin{aligned}
	\lambda_\text{max} T &= 2.9 \times 10^{-3} \text{ mK}
\end{aligned}
$$
**IMPORTANT:** the $\text{mK}$ in the equation above means **'metres kelvin'**, *not milli-kelvin* or something else.

**Apparent brightness** $b$ is an astronomical term used for the **amount of light** from a star or other body that **reaches Earth**. It is the **intensity** of the radiation at the orbit of the Earth.

$b$ has the units $\text{W m}^{-2}$.

The **apparent brightness** of the Sun is known as the solar constant $S$. It is the **power that is incident** on **one square metre** at the top of the Earth's atmosphere.

---

The energy per second transferred to Earth from the Sun is the **overall difference** between the **incoming solar radiation** and the radiation that Earth **re-emits** back into space.

However, the energy from the Sun falls on only **half** of the area of the Earth's sphere at any one time. The energy is then transferred to the **whole** of Earth's surface.

Assuming that the Earth is spherical, it has a surface area of $4\pi R^2$ — but of course, everyone knows what shape the Earth actually is.

So, the average incident intensity $I_\text{surface}$ during 24 hours at any point on the surface must be:
$$
I_\text{surface} = \frac{S\times \pi R^2}{4 \pi R^2} = \frac{S}{4}
$$
$\therefore I_\text{surface}$ has a maximum value of $0.25 \times 1360 = 340 \text{ W}$.

The actual mean power that is incident on each square metre of the Earth's surface is **lower** than the calculated value of $340 \text{ W}$ because the Sun's energy arriving at the surface is **reduced** due to:
- Radiation is absorbed and (Rayleigh *ray-lee*) scattered by the atmosphere
- The radiation passes through a greater thickness of atmosphere at dawn and dusk than in the middle of the day $\implies$ more scattering and absorption take place when the thickness of the atmosphere is greater.
- Extremely in-depth information [here](https://radio.astro.gla.ac.uk/a2_oa/a2oa_sec5.pdf).

![[Pasted image 20250818220444.png]]
*Rayleigh scattering causes my favourite gradient to occur :)*

---

The theoretical **black body** cannot be realised in practice. Real objects are known as grey bodies—their radiated emission is **less** than that of an equivalent black body at the **same temperature**. The extent to which an emitter is imperfect compared with a black body is described by **emissivity**, $e$.
$$
\begin{aligned}
\text{Emissivity} &= \frac{\text{energy radiated from the surface of an object}}{\text{energy radiated from a black body at the same temperature}}\\\\
e &= \frac{\text{power radiated per unit area from the surface of an object}}{\sigma T^4}\\\\
\end{aligned}
$$
Hence,

$$
e=
\begin{cases}
	1 & \text{for a black body}\\
	0 & \text{for a perfectly reflecting and non-radiating object}\\
	0 < x\in\mathbb{R}<1 & \text{otherwise}
\end{cases}
$$

$e$ has no units because it is a ratio of energies.

The Earth's surface is a **grey body** and scatters some of the incident energy back into the **atmosphere**. The extent to which the surface does this is known as its albedo.
$$
\begin{aligned}
	\text{Albedo} &= \frac{\text{energy scattered by a given surface in a given time}}{\text{total energy incident on the surface in the same time}}\\
	&= \frac{\text{total scattered power}}{\text{total incident power}}
\end{aligned}
$$
Different expressions using Albedo can be formed.
Let $\alpha=\text{Albedo}$.
Let $L_{scat} = \text{total scattered power} = \text{total reflected power}$
Let $L_{inci} = \text{total incident power}$
Let $L_{radi} = \text{total radiated power}$
$$
\begin{aligned}
	 \frac{L_{scat} + L_{radi}}{L_{inci}} &= 1\\
	 \frac{L_{scat}}{L_{inci}} &= \alpha\\
	 \therefore \frac{L_{radi}}{L_{inci}} &= 1-\alpha\\
	 L_{radi} &= (1-\alpha)L_{inci}
\end{aligned}
$$
The above derivation has to be **memorised**. The formula in the book of depression is only **partial**, including $L_{scat}$ and $L_{inci}$ and $\alpha$ only.

Note that, in some texts, $L_\odot$ means the luminosity of the sun

---

**Greenhouse gases** to memorise:
- Water vapour ($\text{H}_2\text{O}$)
- Carbon dioxide ($\text{CO}_2$)
- Methane ($\text{CH}_4$)
- Nitrous oxide (dinitrogen monoxide) ($\text{N}_2\text{O}$)
Remember: **N**ot **M**y **C**all, **W**alter (breaking bad heheheheheheeeee)

---
Worked Questions I found difficult
---
A planet orbits at a distance $d$ from a star. The power emitted by the star is $P$. The total surface area of the planet is $A$.
Explain why the power incident on the planet is $\frac{P}{4\pi d^2}=\frac{A}{4}$.
**Answer (unfold):**
	By imagining a sphere with radius $d$ with a centre at the same position as the centre of the star, the intensity of the star at distance $d$ can be obtained. And since $I=P\div A$, the $A$ in $P=AI$ would be the cross-sectional area of the planet which is $\frac{A}{4}$. This is due to the inverse square law.